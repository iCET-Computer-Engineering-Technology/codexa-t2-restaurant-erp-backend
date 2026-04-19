package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.KitchenOrder;
import edu.icet.ecom.repository.KitchenOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class KitchenOrderRepositoryImpl implements KitchenOrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final String COL_GET_TIME = "get_time";

    @Override
    public void createKitchenOrder(Long orderId) {
        String sql = """
            INSERT INTO kitchen_order(order_id, status, get_time)
            VALUES (?, 'in_progress', NOW())
        """;
        jdbcTemplate.update(sql, orderId);
    }

    @Override
    public void createKitchenOrderWithChef(Long orderId, Long chefId, String status) {
        String sql = """
            INSERT INTO kitchen_order(id, order_id, chef_id, status, get_time)
            VALUES (?, ?, ?, ?, NOW())
        """;
        jdbcTemplate.update(sql, orderId, orderId, chefId, status);
    }

    @Override
    public List<KitchenOrder> getKitchenOrders() {
        String sql = "SELECT * FROM kitchen_order WHERE status != 'done'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            KitchenOrder ko = new KitchenOrder();
            ko.setId(rs.getLong("id"));
            ko.setOrderId(rs.getLong("order_id"));
            ko.setChefId(rs.getObject("chef_id") != null ? rs.getLong("chef_id") : null);
            ko.setStatus(rs.getString("status"));
            if (rs.getTimestamp(COL_GET_TIME) != null) {
                ko.setGetTime(rs.getTimestamp(COL_GET_TIME).toLocalDateTime());
            }
            if (rs.getTimestamp("end_time") != null) {
                ko.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            }
            return ko;
        });
    }

    @Override
    public void markAsDone(Long orderId) {
            String sql = """
            UPDATE kitchen_order
            SET status='done', end_time=NOW()
            WHERE order_id=?
        """;

            jdbcTemplate.update(sql, orderId);
        }

    @Override
    public boolean existsByOrderId(Long orderId) {
        String sql = "SELECT COUNT(*) FROM kitchen_order WHERE order_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, orderId);
        return count != null && count > 0;
    }

    @Override
    public KitchenOrder findByOrderId(Long orderId) {
        String sql = "SELECT * FROM kitchen_order WHERE order_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                KitchenOrder ko = new KitchenOrder();
                ko.setId(rs.getLong("id"));
                ko.setOrderId(rs.getLong("order_id"));
                ko.setChefId(rs.getObject("chef_id") != null ? rs.getLong("chef_id") : null);
                ko.setStatus(rs.getString("status"));
                if (rs.getTimestamp(COL_GET_TIME) != null) {
                    ko.setGetTime(rs.getTimestamp(COL_GET_TIME).toLocalDateTime());
                }
                if (rs.getTimestamp("end_time") != null) {
                    ko.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                }
                return ko;
            }, orderId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public KitchenOrder findById(Long id) {
        String sql = "SELECT * FROM kitchen_order WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                KitchenOrder ko = new KitchenOrder();
                ko.setId(rs.getLong("id"));
                ko.setOrderId(rs.getLong("order_id"));
                ko.setChefId(rs.getObject("chef_id") != null ? rs.getLong("chef_id") : null);
                ko.setStatus(rs.getString("status"));
                if (rs.getTimestamp(COL_GET_TIME) != null) {
                    ko.setGetTime(rs.getTimestamp(COL_GET_TIME).toLocalDateTime());
                }
                if (rs.getTimestamp("end_time") != null) {
                    ko.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                }
                return ko;
            }, id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void assignChef(Long kitchenOrderId, Long chefId) {
        String sql = "UPDATE kitchen_order SET chef_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, chefId, kitchenOrderId);
    }

    @Override
    public int countActiveOrdersByChefId(Long chefId) {
        String sql = "SELECT COUNT(*) FROM kitchen_order WHERE chef_id = ? AND status IN ('pending', 'in_progress')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, chefId);
        return count != null ? count : 0;
    }

    @Override
    public void markAsReady(Long orderId) {
        String sql = """
        UPDATE kitchen_order
        SET status='ready', end_time=NOW()
        WHERE order_id=?
    """;
        jdbcTemplate.update(sql, orderId);
    }
}
