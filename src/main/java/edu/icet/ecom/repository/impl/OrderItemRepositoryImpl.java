package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.*;

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
            return jdbcTemplate.query(sql, (rs, rowNum) -> mapOrderItem(rs), orderId);
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public OrderItem findById(Integer orderItemId) {
        if (orderItemId == null || orderItemId <= 0) {
            return null;
        }
        String sql = "SELECT id, order_id, menu_item_id, portion_id, quantity, price, status, notes, created_at FROM order_items WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapOrderItem(rs), orderItemId);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public boolean updateStatus(Integer orderItemId, String status) {
        String sql = "UPDATE order_items SET status = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status, orderItemId) > 0;
    }

    @Override
    public Integer saveAndGetId(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, portion_id, quantity, price, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuItemId());
            ps.setInt(3, orderItem.getPortionId());
            ps.setInt(4, orderItem.getQuantity());
            ps.setBigDecimal(5, orderItem.getPrice());
            ps.setString(6, orderItem.getStatus());
            ps.setString(7, orderItem.getNotes());
            return ps;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue)
                .orElseThrow(() -> new RuntimeException("Order item insert failed - no generated key returned"));
    }

    private OrderItem mapOrderItem(java.sql.ResultSet rs) throws java.sql.SQLException {
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
    }

    @Override
    public List<Map<String, Object>> findItemsWithNamesByOrderId(Integer orderId) {
        String sql = """
            SELECT 
                oi.id,
                mi.name as item_name,
                p.portion_name as portion_name,
                oi.quantity,
                oi.price,
                (oi.quantity * oi.price) as line_total,
                oi.notes
            FROM order_items oi
            LEFT JOIN menu_items mi ON oi.menu_item_id = mi.id
            LEFT JOIN portions p ON oi.portion_id = p.id
            WHERE oi.order_id = ?
            ORDER BY oi.id
            """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", rs.getInt("id"));
            result.put("item_name", rs.getString("item_name"));
            result.put("portion_name", rs.getString("portion_name"));
            result.put("quantity", rs.getInt("quantity"));
            result.put("price", rs.getBigDecimal("price"));
            result.put("line_total", rs.getBigDecimal("line_total"));
            result.put("notes", rs.getString("notes"));
            return result;
        }, orderId);
    }

    @Override
    public List<Map<String, Object>> findItemsWithNamesByOrderIds(List<Integer> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        String placeholders = String.join(",", Collections.nCopies(orderIds.size(), "?"));
        String sql = String.format("""
            SELECT 
                oi.id,
                oi.order_id,
                mi.name as item_name,
                p.portion_name as portion_name,
                oi.quantity,
                oi.price,
                (oi.quantity * oi.price) as line_total,
                oi.notes
            FROM order_items oi
            LEFT JOIN menu_items mi ON oi.menu_item_id = mi.id
            LEFT JOIN portions p ON oi.portion_id = p.id
            WHERE oi.order_id IN (%s)
            ORDER BY oi.order_id, oi.id
            """, placeholders);
        
        return jdbcTemplate.query(sql, orderIds.toArray(), (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", rs.getInt("id"));
            result.put("order_id", rs.getInt("order_id"));
            result.put("item_name", rs.getString("item_name"));
            result.put("portion_name", rs.getString("portion_name"));
            result.put("quantity", rs.getInt("quantity"));
            result.put("price", rs.getBigDecimal("price"));
            result.put("line_total", rs.getBigDecimal("line_total"));
            result.put("notes", rs.getString("notes"));
            return result;
        });
    }
}
