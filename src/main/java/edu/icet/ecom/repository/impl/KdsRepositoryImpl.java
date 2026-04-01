package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.KdsOrder;
import edu.icet.ecom.repository.KdsRepository;
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
public class KdsRepositoryImpl implements KdsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer saveAndGetId(KdsOrder kdsOrder) {
        String sql = "INSERT INTO kds_orders (order_id, is_rush, is_vip, color_status) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, kdsOrder.getOrderId());
            ps.setBoolean(2, kdsOrder.getIsRush() != null ? kdsOrder.getIsRush() : false);
            ps.setBoolean(3, kdsOrder.getIsVip() != null ? kdsOrder.getIsVip() : false);
            ps.setString(4, kdsOrder.getColorStatus() != null ? kdsOrder.getColorStatus() : "green");
            return ps;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue)
                .orElseThrow(() -> new RuntimeException("KDS order insert failed - no generated key"));
    }

    @Override
    public Integer saveKdsOrderItem(Integer kdsOrderId, Integer orderItemId) {
        String sql = "INSERT INTO kds_order_items (kds_order_id, order_item_id, status) VALUES (?, ?, 'pending')";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, kdsOrderId);
            ps.setInt(2, orderItemId);
            return ps;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey()).map(Number::intValue)
                .orElseThrow(() -> new RuntimeException("KDS order item insert failed"));
    }

    @Override
    public KdsOrder findByOrderId(Integer orderId) {
        if (orderId == null || orderId <= 0) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, order_id, displayed_at, bumped_at, bumped_by, is_rush, is_vip, color_status FROM kds_orders WHERE order_id = ?",
                    (rs, rowNum) -> new KdsOrder(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getObject("displayed_at", java.time.LocalDateTime.class),
                            rs.getObject("bumped_at", java.time.LocalDateTime.class),
                            rs.getObject("bumped_by", Integer.class),
                            rs.getBoolean("is_rush"),
                            rs.getBoolean("is_vip"),
                            rs.getString("color_status")
                    ),
                    orderId
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<KdsOrder> findAll() {
        return jdbcTemplate.query(
                "SELECT id, order_id, displayed_at, bumped_at, bumped_by, is_rush, is_vip, color_status FROM kds_orders ORDER BY displayed_at DESC",
                (rs, rowNum) -> new KdsOrder(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getObject("displayed_at", java.time.LocalDateTime.class),
                        rs.getObject("bumped_at", java.time.LocalDateTime.class),
                        rs.getObject("bumped_by", Integer.class),
                        rs.getBoolean("is_rush"),
                        rs.getBoolean("is_vip"),
                        rs.getString("color_status")
                )
        );
    }

    @Override
    public boolean updateKdsItemStatus(Integer kdsOrderItemId, String status) {
        if (kdsOrderItemId == null || kdsOrderItemId <= 0 || status == null) {
            return false;
        }
        String sql = "UPDATE kds_order_items SET status = ?, fired_at = CASE WHEN ? = 'in_progress' THEN NOW() ELSE fired_at END, " +
                "completed_at = CASE WHEN ? = 'done' THEN NOW() ELSE completed_at END WHERE id = ?";
        return jdbcTemplate.update(sql, status, status, status, kdsOrderItemId) > 0;
    }

    @Override
    public List<KdsOrder> findAllPending() {
        return jdbcTemplate.query(
                "SELECT DISTINCT k.id, k.order_id, k.displayed_at, k.bumped_at, k.bumped_by, k.is_rush, k.is_vip, k.color_status " +
                        "FROM kds_orders k " +
                        "LEFT JOIN kds_order_items ki ON k.id = ki.kds_order_id " +
                        "WHERE ki.status IN ('pending', 'in_progress')",
                (rs, rowNum) -> new KdsOrder(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getObject("displayed_at", java.time.LocalDateTime.class),
                        rs.getObject("bumped_at", java.time.LocalDateTime.class),
                        rs.getObject("bumped_by", Integer.class),
                        rs.getBoolean("is_rush"),
                        rs.getBoolean("is_vip"),
                        rs.getString("color_status")
                )
        );
    }
}

