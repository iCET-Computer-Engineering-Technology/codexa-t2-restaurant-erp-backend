package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<OrderItem> findByOrderId(Integer orderId) {
        if (orderId == null || orderId <= 0) {
            return List.of();
        }
        String sql = "SELECT id, order_id, menu_item_id, portion_id, quantity, price, status, notes, created_at " +
                "FROM order_items WHERE order_id = ? ORDER BY id ASC";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setMenuItemId(rs.getInt("menu_item_id"));
                item.setPortionId(rs.getInt("portion_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setStatus(rs.getString("status"));
                item.setNotes(rs.getString("notes"));
                item.setCreatedAt(rs.getObject("created_at", java.time.LocalDateTime.class));
                return item;
            }, orderId);
        } catch (Exception e) {
            return List.of(); // Return empty list instead of null on error
        }
    }

    @Override
    public Integer saveAndGetId(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, portion_id, quantity, price, status, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuItemId());
            ps.setInt(3, orderItem.getPortionId());
            ps.setInt(4, orderItem.getQuantity());
            ps.setBigDecimal(5, orderItem.getPrice());
            ps.setString(6, orderItem.getStatus());
            ps.setString(7, orderItem.getNotes());
            return ps;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue).orElseThrow(() -> new RuntimeException("Order item insert failed - no generated key returned"));
    }
}
