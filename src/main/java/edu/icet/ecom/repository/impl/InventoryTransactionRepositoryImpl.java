package edu.icet.ecom.repository.impl;

import edu.icet.ecom.model.InventoryDeductionError;
import edu.icet.ecom.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class InventoryTransactionRepositoryImpl implements InventoryTransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertDeductionTransaction(Integer ingredientId, BigDecimal quantity, BigDecimal balanceAfter, Integer orderId, String notes) {
        String sql = """
                INSERT INTO inventory_transactions (ingredient_id, transaction_type, quantity, balance_after, order_id, notes)
                VALUES (?, 'deduction', ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, ingredientId, quantity, balanceAfter, orderId, notes);
    }

    @Override
    public void insertDeductionError(InventoryDeductionError error) {
        String sql = """
                INSERT INTO inventory_deduction_errors
                (order_item_id, order_id, ingredient_id, required_quantity, available_quantity, reason)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql,
                error.getOrderItemId(),
                error.getOrderId(),
                error.getIngredientId(),
                error.getRequiredQuantity(),
                error.getAvailableQuantity(),
                error.getReason());
    }

    @Override
    public boolean wasOrderItemAlreadyDeducted(Integer orderItemId) {
        String sql = "SELECT COUNT(*) FROM inventory_deduction_audit WHERE order_item_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, orderItemId);
        return count != null && count > 0;
    }

    @Override
    public boolean markOrderItemDeducted(Integer orderItemId) {
        String sql = "INSERT INTO inventory_deduction_audit (order_item_id) VALUES (?)";
        try {
            return jdbcTemplate.update(sql, orderItemId) > 0;
        } catch (DuplicateKeyException ex) {
            return false;
        }
    }
}

