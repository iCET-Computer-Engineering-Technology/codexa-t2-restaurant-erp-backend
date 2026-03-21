package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "SELECT oi.*, mi.name AS item_name " +
                "FROM order_items oi " +
                "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                "WHERE oi.order_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            OrderItem item = new OrderItem();
            item.setId(rs.getLong("id"));
            item.setOrderId(rs.getLong("order_id"));
            item.setMenuItemId(rs.getLong("menu_item_id"));
            item.setItemName(rs.getString("item_name"));
            item.setQuantity(rs.getInt("quantity"));
            item.setUnitPrice(rs.getBigDecimal("price"));
            item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return item;
        }, orderId);
    }

    @Override
    public Long saveAndGetId(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, total_price, created_at) "+
                "VALUES (?, ?, ?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, orderItem.getOrderId());
            ps.setLong(2, orderItem.getMenuItemId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setBigDecimal(4, orderItem.getUnitPrice());
            ps.setBigDecimal(5, orderItem.getTotalPrice());
            return ps;
        }, keyHolder);

        return Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .orElseThrow(()->new RuntimeException("order insert failed: no generated key returned"))
                ;
    }
}
