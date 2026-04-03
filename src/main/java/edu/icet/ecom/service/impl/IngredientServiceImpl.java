package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.IngredientDto;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.mapper.IngredientMapper;
import edu.icet.ecom.service.IngredientService;
import edu.icet.ecom.service.WebSocketNotificationService;
import edu.icet.ecom.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final JdbcTemplate jdbcTemplate;
    private final WebSocketNotificationService webSocketNotificationService;

    @Override
    public void add(IngredientDto ingredientDto) {
        jdbcTemplate.update(
                "INSERT INTO ingredients(name, current_stock, cost_per_unit, unit) values(?,?,?,?)",
                ingredientDto.getName(),
                ingredientDto.getQuantity(),
                ingredientDto.getPrice(),
                ingredientDto.getDescription()
        );
    }

    @Override
    public IngredientDto get(Integer id) {
        List<IngredientDto> list = jdbcTemplate.query(
                "SELECT id, name, current_stock AS quantity, cost_per_unit AS price, unit AS description FROM ingredients WHERE id = ?",
                new IngredientMapper(),
                id
        );
        return list.stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
    }

    @Override
    public List<IngredientDto> getAll(int page, int size) {
        int offset = Math.max(page, 0) * Math.max(size, 1);
        return jdbcTemplate.query(
                "SELECT id, name, current_stock AS quantity, cost_per_unit AS price, unit AS description FROM ingredients LIMIT ? OFFSET ?",
                new IngredientMapper(),
                Math.max(size, 1),
                offset
        );
    }

    @Override
    public IngredientDto update(Integer id, IngredientDto dto) {
        int count = jdbcTemplate.update(
                "UPDATE ingredients SET name = ?, current_stock = ?, cost_per_unit = ?, unit = ? WHERE id = ?",
                dto.getName(), dto.getQuantity(), dto.getPrice(), dto.getDescription(), id
        );
        if (count == 0) {
            throw new ResourceNotFoundException("Ingredient not found id " + id);
        }
        return dto;
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM ingredients WHERE id = ?", id);
    }
    
    @Override
    @Transactional
    public void deductInventoryForOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) return;
        
        for (OrderItem item : orderItems) {
            String sql = "SELECT i.id AS ingredient_id, ri.quantity AS required_qty, i.current_stock, i.unit " +
                         "FROM recipe_ingredients ri " +
                         "JOIN recipes r ON r.id = ri.recipe_id " +
                         "JOIN ingredients i ON i.id = ri.ingredient_id " +
                         "WHERE r.menu_item_id = ?";
            
            List<Map<String, Object>> ingredientsToDeduct = jdbcTemplate.queryForList(sql, item.getMenuItemId());
            
            for (Map<String, Object> row : ingredientsToDeduct) {
                Integer ingredientId = (Integer) row.get("ingredient_id");
                BigDecimal reqQtyPerItem = (BigDecimal) row.get("required_qty");
                BigDecimal currentStock = (BigDecimal) row.get("current_stock");
                String unit = (String) row.get("unit");
                
                BigDecimal totalDeduction = reqQtyPerItem.multiply(BigDecimal.valueOf(item.getQuantity()));
                BigDecimal newStock = currentStock.subtract(totalDeduction);
                
                jdbcTemplate.update("UPDATE ingredients SET current_stock = ? WHERE id = ?", newStock, ingredientId);
                
                // Broadcast WebSocket event
                try {
                    webSocketNotificationService.notifyInventoryStockUpdate(ingredientId, newStock, unit);
                } catch (Exception e) {
                    System.err.println("Failed to broadcast inventory update: " + e.getMessage());
                }
            }
        }
    }
}
