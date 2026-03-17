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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<OrderItem> findByOrderId(Integer orderId) {
        String sql = "SELECT id, order_id, menu_item_id, quantity, unit_price, " +
                "modifier_total, line_total, course_number, status, notes, created_at " +
                "FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            OrderItem item = new OrderItem();
            item.setId(rs.getInt("id"));
            item.setOrderId(rs.getInt("order_id"));
            item.setMenuItemId(rs.getInt("menu_item_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setUnitPrice(rs.getBigDecimal("unit_price"));
            item.setModifierTotal(rs.getBigDecimal("modifier_total"));
            item.setLineTotal(rs.getBigDecimal("line_total"));
            item.setCourseNumber(rs.getInt("course_number"));
            item.setStatus(rs.getString("status"));
            item.setNotes(rs.getString("notes"));
            item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return item;
        }, orderId);
    }

    @Override
    public Integer saveAndGetId(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price, modifier_total, line_total, " +
                " course_number, status, notes, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getMenuItemId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setBigDecimal(4, orderItem.getUnitPrice());
            ps.setBigDecimal(5, orderItem.getModifierTotal());
            ps.setBigDecimal(6, orderItem.getLineTotal());
            ps.setInt(7, orderItem.getCourseNumber() != null ? orderItem.getCourseNumber() : 1);
            ps.setString(8, orderItem.getStatus() != null ? orderItem.getStatus() : "pending");
            ps.setString(9, orderItem.getNotes());
            return ps;
        }, keyHolder);

        Integer generatedId = Optional.ofNullable(keyHolder.getKey())
                .map(Number::intValue)
                .orElseThrow(() -> new RuntimeException("OrderItem insert failed: no generated key returned"));

        // ── Set createdAt so it's populated in the respons
        orderItem.setCreatedAt(LocalDateTime.now());
        return generatedId;
    }
}
