package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.InventoryDeductionResponse;
import edu.icet.ecom.dto.LowStockAlertDto;
import edu.icet.ecom.dto.StockDiscrepancyDto;
import edu.icet.ecom.entity.Ingredient;
import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.entity.RecipeIngredient;
import edu.icet.ecom.model.InventoryDeductionError;
import edu.icet.ecom.repository.IngredientInventoryRepository;
import edu.icet.ecom.repository.InventoryAlertRepository;
import edu.icet.ecom.repository.InventoryTransactionRepository;
import edu.icet.ecom.repository.OrderItemRepository;
import edu.icet.ecom.repository.RecipeRepository;
import edu.icet.ecom.repository.StockCountSessionRepository;
import edu.icet.ecom.service.InventoryService;
import edu.icet.ecom.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final OrderItemRepository orderItemRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientInventoryRepository ingredientInventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final StockCountSessionRepository stockCountSessionRepository;
    private final InventoryAlertRepository inventoryAlertRepository;
    private final WebSocketNotificationService webSocketNotificationService;

    @Override
    @Transactional
    public InventoryDeductionResponse handleFiredStatus(Integer orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId);
        if (orderItem == null) {
            return new InventoryDeductionResponse(false, "Order item not found");
        }

        if (inventoryTransactionRepository.wasOrderItemAlreadyDeducted(orderItemId)) {
            return new InventoryDeductionResponse(true, "Inventory already deducted");
        }

        List<RecipeIngredient> recipeIngredients = recipeRepository.findCurrentRecipeIngredientsByMenuItemId(orderItem.getMenuItemId());
        if (recipeIngredients.isEmpty()) {
            String reason = "Recipe not found for menu item " + orderItem.getMenuItemId();
            logAndPersistError(orderItem, null, BigDecimal.ZERO, BigDecimal.ZERO, reason);
            return new InventoryDeductionResponse(false, reason);
        }

        Map<Integer, BigDecimal> requiredByIngredient = calculateRequiredQuantities(recipeIngredients, orderItem.getQuantity());
        List<Integer> ingredientIds = requiredByIngredient.keySet().stream().toList();

        List<Ingredient> lockedIngredients = ingredientInventoryRepository.findByIdsForUpdate(ingredientIds);
        Map<Integer, Ingredient> ingredientById = lockedIngredients.stream()
                .collect(Collectors.toMap(Ingredient::getId, ingredient -> ingredient));
        Map<Integer, BigDecimal> stockMap = lockedIngredients.stream().collect(
                Collectors.toMap(Ingredient::getId, ingredient -> ingredient.getCurrentStock() == null ? BigDecimal.ZERO : ingredient.getCurrentStock())
        );

        for (Map.Entry<Integer, BigDecimal> entry : requiredByIngredient.entrySet()) {
            Integer ingredientId = entry.getKey();
            BigDecimal requiredQty = entry.getValue();
            BigDecimal availableQty = stockMap.getOrDefault(ingredientId, BigDecimal.ZERO);
            if (availableQty.compareTo(requiredQty) < 0) {
                logAndPersistError(orderItem, ingredientId, requiredQty, availableQty, "Insufficient inventory");
                return new InventoryDeductionResponse(false, "Insufficient stock for ingredient ID " + ingredientId);
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : requiredByIngredient.entrySet()) {
            Integer ingredientId = entry.getKey();
            BigDecimal requiredQty = entry.getValue();

            boolean deducted = ingredientInventoryRepository.deductStock(ingredientId, requiredQty);
            if (!deducted) {
                BigDecimal latestAvailable = ingredientInventoryRepository.findCurrentStock(ingredientId);
                logAndPersistError(orderItem, ingredientId, requiredQty, latestAvailable, "Stock changed while deducting");
                return new InventoryDeductionResponse(false, "Deduction cancelled due to concurrent stock change");
            }

            BigDecimal balanceAfter = ingredientInventoryRepository.findCurrentStock(ingredientId);
            inventoryTransactionRepository.insertDeductionTransaction(
                    ingredientId,
                    requiredQty,
                    balanceAfter,
                    orderItem.getOrderId(),
                    "Auto deduction when order item moved to fired"
            );

            evaluateLowStockAndNotify(ingredientById.get(ingredientId), balanceAfter);
        }

        boolean marked = inventoryTransactionRepository.markOrderItemDeducted(orderItemId);
        if (!marked) {
            return new InventoryDeductionResponse(true, "Inventory already deducted");
        }

        return new InventoryDeductionResponse(true, "Inventory deducted successfully");
    }

    @Override
    public List<StockDiscrepancyDto> getDiscrepancyReport(Integer sessionId) {
        return stockCountSessionRepository.getDiscrepancyReport(sessionId);
    }

    @Override
    public List<LowStockAlertDto> getActiveLowStockAlerts() {
        List<LowStockAlertDto> alerts = inventoryAlertRepository.findActiveLowStockAlerts();
        for (LowStockAlertDto alert : alerts) {
            alert.setReorderLink(buildReorderLink(alert.getIngredientId()));
        }
        return alerts;
    }

    @Override
    @Transactional
    public void updateLowStockThreshold(Integer ingredientId, BigDecimal threshold) {
        if (ingredientId == null || ingredientId <= 0) {
            throw new IllegalArgumentException("Invalid ingredientId");
        }
        if (threshold == null || threshold.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Threshold must be zero or greater");
        }

        Ingredient ingredient = ingredientInventoryRepository.findById(ingredientId);
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient not found");
        }

        ingredientInventoryRepository.updateLowStockThreshold(ingredientId, threshold);
        ingredient.setLowStockThreshold(threshold);

        BigDecimal currentStock = ingredient.getCurrentStock() == null ? BigDecimal.ZERO : ingredient.getCurrentStock();
        evaluateLowStockAndNotify(ingredient, currentStock);
    }

    private Map<Integer, BigDecimal> calculateRequiredQuantities(List<RecipeIngredient> recipeIngredients, Integer orderQty) {
        Map<Integer, BigDecimal> requiredByIngredient = new HashMap<>();
        BigDecimal orderQuantity = BigDecimal.valueOf(orderQty == null || orderQty <= 0 ? 1 : orderQty);

        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            BigDecimal recipeQty = recipeIngredient.getQuantity() == null ? BigDecimal.ZERO : recipeIngredient.getQuantity();
            BigDecimal totalRequired = recipeQty.multiply(orderQuantity);
            requiredByIngredient.merge(recipeIngredient.getIngredientId(), totalRequired, BigDecimal::add);
        }
        return requiredByIngredient;
    }

    private void evaluateLowStockAndNotify(Ingredient ingredient, BigDecimal currentStock) {
        if (ingredient == null || ingredient.getId() == null) {
            return;
        }

        BigDecimal threshold = ingredient.getLowStockThreshold();
        if (threshold == null) {
            // Threshold is DB-driven; if not configured, keep alerts cleared.
            inventoryAlertRepository.resolveLowStockAlerts(ingredient.getId());
            return;
        }

        if (currentStock.compareTo(threshold) <= 0) {
            boolean hasActiveAlert = inventoryAlertRepository.existsActiveLowStockAlert(ingredient.getId());
            if (!hasActiveAlert) {
                inventoryAlertRepository.createLowStockAlert(ingredient.getId());
                webSocketNotificationService.notifyInventoryStockUpdate(
                        ingredient.getId(),
                        currentStock,
                        ingredient.getUnit()
                );
            }
            return;
        }

        inventoryAlertRepository.resolveLowStockAlerts(ingredient.getId());
    }

    private String buildReorderLink(Integer ingredientId) {
        return "/app/reorder?ingredientId=" + ingredientId;
    }

    private void logAndPersistError(OrderItem orderItem,
                                    Integer ingredientId,
                                    BigDecimal requiredQty,
                                    BigDecimal availableQty,
                                    String reason) {
        InventoryDeductionError error = new InventoryDeductionError();
        error.setOrderItemId(orderItem.getId());
        error.setOrderId(orderItem.getOrderId());
        error.setIngredientId(ingredientId);
        error.setRequiredQuantity(requiredQty);
        error.setAvailableQuantity(availableQty);
        error.setReason(reason);

        inventoryTransactionRepository.insertDeductionError(error);

        log.error("Inventory deduction blocked. orderItemId={}, orderId={}, ingredientId={}, required={}, available={}, reason={}",
                orderItem.getId(),
                orderItem.getOrderId(),
                ingredientId,
                requiredQty,
                availableQty,
                reason);
    }
}
