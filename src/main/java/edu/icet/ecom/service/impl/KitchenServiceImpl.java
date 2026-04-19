package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.InventoryDeductionResponse;
import edu.icet.ecom.entity.*;
import edu.icet.ecom.repository.*;
import edu.icet.ecom.service.InventoryService;
import edu.icet.ecom.service.KitchenService;
import edu.icet.ecom.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private static final Logger log = LoggerFactory.getLogger(KitchenServiceImpl.class);

    private static final String ORDER_TYPE_DINE_IN = "dine_in";
    private static final String ORDER_TYPE_TAKEOUT = "takeout";
    private static final String ORDER_TYPE_BOOKING = "booking";

    private static final Set<String> VALID_ORDER_TYPES = Set.of(ORDER_TYPE_DINE_IN, ORDER_TYPE_TAKEOUT, ORDER_TYPE_BOOKING);
    private static final Set<String> VALID_ORDER_ITEM_STATUSES = Set.of("pending", "in_progress", "preparing", "fired", "ready", "served", "voided");

    private final OrderRepository orderRepository;
    private final WaiterRepository waiterRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final KitchenOrderRepository kitchenOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryService inventoryService;
    private final UserRepository userRepository;
    private final WebSocketNotificationService webSocketNotificationService;

    @Override
    public List<KitchenOrder> getKitchenOrders() {
        List<KitchenOrder> allOrders = kitchenOrderRepository.getKitchenOrders();
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CHEF"))) {
            UserEntity chef = userRepository.findByUsername(auth.getName());
            if (chef != null) {
                return allOrders.stream()
                        .filter(ko -> chef.getId().equals(ko.getChefId()))
                        .collect(toList());
            }
        }
        return allOrders;
    }

    @Override
    public List<Waiter> getActiveWaiters() {
        return waiterRepository.findActiveWaiters();
    }

    @Override
    @Transactional
    public void assignWaiter(Long kitchenOrderId, Long waiterId) {
        KitchenOrder ko = kitchenOrderRepository.findById(kitchenOrderId);
        if (ko == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kitchen order not found");
        }

        if (!"ready".equalsIgnoreCase(ko.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order not ready for assignment");
        }

        boolean assigned = orderAssignmentRepository.existsByKitchenOrderId(kitchenOrderId);

        if (assigned) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order already assigned to a waiter");
        }

        orderAssignmentRepository.assignWaiter(kitchenOrderId, waiterId);
        waiterRepository.updateWaiterStatus(waiterId, "busy");

        publishSnapshotAfterCommit();
    }

    @Override
    public List<WaiterDetails> getAssignmentsWaiter() {
        return orderAssignmentRepository.getAssignments();
    }

    @Override
    public List<Order> getOpenOrders() {
        List<Order> orders = orderRepository.findAll().stream()
                .filter(o -> "open".equals(o.getStatus()) || "sent_to_kitchen".equals(o.getStatus()) || "partially_ready".equals(o.getStatus()))
                .collect(toList());
        orders.forEach(order ->
                order.setItems(
                        orderItemRepository.findByOrderId(order.getId())
                )
        );
        return orders;
    }

    @Override
    @Transactional
    public void sendToKitchen(Long orderId) {
        Order order = orderRepository.findById(orderId.intValue());
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        String normalizedType = normalize(order.getOrderType());
        if (!VALID_ORDER_TYPES.contains(normalizedType)) {
            throw new IllegalArgumentException("Unsupported order type for kitchen routing: " + order.getOrderType());
        }

        boolean exists = kitchenOrderRepository.existsByOrderId(orderId);
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order already sent to kitchen");
        }

        // AUTO ASSIGN CHEF
        Long assignedChefId = autoAssignChef();
        
        if (assignedChefId != null) {
            // Chef available - assign and start immediately
            kitchenOrderRepository.createKitchenOrderWithChef(orderId, assignedChefId, "in_progress");
            log.info("✅ Order #{} auto-assigned to chef ID: {}", orderId, assignedChefId);
        } else {
            // No chef available - queue for manual assignment
            kitchenOrderRepository.createKitchenOrderWithChef(orderId, null, "pending");
            log.warn("⏳ Order #{} queued - no chef available for auto-assignment", orderId);
        }

        orderRepository.updateStatus(orderId.intValue(), "sent_to_kitchen");

        log.info("{}", buildLabel(order, "KITCHEN_RECEIVED", normalizedType));

        publishSnapshotAfterCommit();
    }

    @Override
    @Transactional
    public void markOrderReady(Long orderId) {
        KitchenOrder ko = kitchenOrderRepository.findByOrderId(orderId);
        if (ko == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kitchen order not found for orderId=" + orderId);
        }

        String current = ko.getStatus() == null ? "" : ko.getStatus().trim().toLowerCase();
        if ("ready".equals(current) || "done".equals(current)) {
            return;
        }

        kitchenOrderRepository.markAsReady(orderId);
        orderRepository.updateStatus(Math.toIntExact(orderId), "partially_ready");
        publishSnapshotAfterCommit();
    }

    @Override
    @Transactional
    public InventoryDeductionResponse updateOrderItemStatus(Integer orderItemId, String status) {
        if (orderItemId == null || orderItemId <= 0) {
            throw new IllegalArgumentException("Invalid orderItemId");
        }

        String normalizedStatus = normalize(status);
        if (!VALID_ORDER_ITEM_STATUSES.contains(normalizedStatus)) {
            throw new IllegalArgumentException("Unsupported order item status: " + status);
        }

        if (isDeductionTriggerStatus(normalizedStatus)) {
            boolean updated = orderItemRepository.updateStatus(orderItemId, "fired");
            if (!updated) {
                throw new IllegalArgumentException("Order item not found");
            }
            InventoryDeductionResponse response = inventoryService.handleFiredStatus(orderItemId);
            publishSnapshotAfterCommit();
            return response;
        }

        boolean updated = orderItemRepository.updateStatus(orderItemId, normalizedStatus);
        if (!updated) {
            throw new IllegalArgumentException("Order item not found");
        }

        publishSnapshotAfterCommit();
        return new InventoryDeductionResponse(false, "Order item status updated");
    }

    private boolean isDeductionTriggerStatus(String status) {
        return "fired".equals(status) || "preparing".equals(status) || "in_progress".equals(status);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String buildLabel(Order order, String status, String type) {
        return String.format("[%s][%s] Order #%s", status, type.toUpperCase(Locale.ROOT), order.getOrderNumber());
    }

    private void publishSnapshotAfterCommit() {
        Runnable publishAction = () -> {
            try {
                webSocketNotificationService.notifyKDSOrdersSnapshot(getOpenOrders());
            } catch (Exception e) {
                log.error("Failed to broadcast KDS snapshot", e);
            }
        };

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishAction.run();
                }
            });
            return;
        }

        publishAction.run();
    }

    @Override
    @Transactional
    public void assignChef(Long kitchenOrderId, Long chefId) {
        KitchenOrder ko = kitchenOrderRepository.findById(kitchenOrderId);
        if (ko == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kitchen Order not found");
        }

        int activeCount = kitchenOrderRepository.countActiveOrdersByChefId(chefId);
        if (activeCount >= 5) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chef cannot be assigned to more than 5 active orders");
        }

        kitchenOrderRepository.assignChef(kitchenOrderId, chefId);
        publishSnapshotAfterCommit();
    }

    @Override
    public List<edu.icet.ecom.dto.AvailableChefDto> getAvailableChefs() {
        return userRepository.findByRole("ROLE_CHEF").stream()
                .filter(chef -> Boolean.TRUE.equals(chef.getIsOnline()))
                .map(chef -> new edu.icet.ecom.dto.AvailableChefDto(chef, kitchenOrderRepository.countActiveOrdersByChefId(chef.getId())))
                .filter(dto -> dto.getActiveOrdersCount() < 5)
                .collect(toList());
    }

    @Override
    public List<edu.icet.ecom.dto.AvailableWaiterDto> getAvailableWaiters() {
        return userRepository.findByRole("ROLE_WAITER").stream()
                .filter(waiter -> Boolean.TRUE.equals(waiter.getIsOnline()))
                .map(waiter -> new edu.icet.ecom.dto.AvailableWaiterDto(waiter, orderAssignmentRepository.countActiveOrdersByWaiterId(waiter.getId())))
                .filter(dto -> dto.getActiveOrdersCount() < 5)  // Assuming max 5 active orders similar to chefs
                .collect(toList());
    }


     //Auto-assigns order to the least busy available chef
     //@return Chef ID if available, null if no chef available
    private Long autoAssignChef() {
        List<edu.icet.ecom.dto.AvailableChefDto> availableChefs = getAvailableChefs();
        
        if (availableChefs.isEmpty()) {
            log.warn("No chefs available for auto-assignment");
            return null;
        }
        
        //Assign to chef with LEAST active orders (load balancing)
        edu.icet.ecom.dto.AvailableChefDto selectedChef = availableChefs.stream()
                .min(Comparator.comparingInt(edu.icet.ecom.dto.AvailableChefDto::getActiveOrdersCount))
                .orElse(null);
        
        if (selectedChef != null && selectedChef.getChef() != null) {
            log.info("Auto-assigned to chef ID: {} (current load: {} orders)", 
                     selectedChef.getChef().getId(), 
                     selectedChef.getActiveOrdersCount());
            return selectedChef.getChef().getId();
        }
        
        return null;
    }
}
