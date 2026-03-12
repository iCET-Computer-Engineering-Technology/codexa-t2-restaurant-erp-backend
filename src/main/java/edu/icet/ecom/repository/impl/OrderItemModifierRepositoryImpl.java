package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.OrderItemModifier;
import edu.icet.ecom.repository.OrderItemModifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemModifierRepositoryImpl implements OrderItemModifierRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int save(OrderItemModifier orderItemModifier) {
        String sql = "INSERT INTO order_item_modifiers (order_item_id, modifier_id, modifier_name, price_adjustment, quantity, created_at)+" +
                "VALUES (?, ?, ?, ?, ?, NOW())";

        return jdbcTemplate.update(sql,
                orderItemModifier.getOrderItemId(),
                orderItemModifier.getModifierId(),
                orderItemModifier.getModifierName(),
                orderItemModifier.getPriceAdjustment(),
                orderItemModifier.getQuantity()
        );
    }
}
