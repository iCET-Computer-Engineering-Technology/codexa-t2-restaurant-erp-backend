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
    public void markAsDone(Integer orderId) {
        String sql = """
            UPDATE kitchen_order
            SET status='done', end_time=NOW()
            WHERE order_id=?
        """;
        jdbcTemplate.update(sql, orderId);
    }

    @Override
    public void createKitchenOrder(Long orderId) {
        String sql = """
            INSERT INTO kitchen_order(order_id, status, get_time)
            VALUES (?, 'in_progress', NOW())
        """;
        jdbcTemplate.update(sql, orderId);
    }

    @Override
    public List<KitchenOrder> getKitchenOrders() {
        String sql = "SELECT * FROM kitchen_order WHERE status != 'done'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            KitchenOrder ko = new KitchenOrder();
            ko.setId(rs.getLong("id"));
            ko.setOrderId(rs.getLong("order_id"));
            ko.setStatus(rs.getString("status"));
            ko.setGetTime(rs.getTimestamp(COL_GET_TIME).toLocalDateTime());
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
        String sql = "SELECT COUNT(*) FROM kitchen_order WHERE order_id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, orderId);
        return count != null && count > 0;
    }

    @Override
    public KitchenOrder findByOrderId(Long orderId) {
        String sql = "SELECT * FROM kitchen_order WHERE order_id = ?";

        List<KitchenOrder> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            KitchenOrder ko = new KitchenOrder();
            ko.setId(rs.getLong("id"));
            ko.setOrderId(rs.getLong("order_id"));
            ko.setStatus(rs.getString("status"));

            if (rs.getTimestamp(COL_GET_TIME) != null) {
                ko.setGetTime(rs.getTimestamp(COL_GET_TIME).toLocalDateTime());
            }

            if (rs.getTimestamp("end_time") != null) {
                ko.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            }

            return ko;
        }, orderId);

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public KitchenOrder findById(Long id) {
        String sql = "SELECT * FROM kitchen_order WHERE id = ?";

        List<KitchenOrder> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            KitchenOrder ko = new KitchenOrder();
            ko.setId(rs.getLong("id"));
            ko.setOrderId(rs.getLong("order_id"));
            ko.setStatus(rs.getString("status"));

            if (rs.getTimestamp("get_time") != null) {
                ko.setGetTime(rs.getTimestamp("get_time").toLocalDateTime());
            }

            if (rs.getTimestamp("end_time") != null) {
                ko.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            }

            return ko;
        }, id);

        return list.isEmpty() ? null : list.get(0);
    }
}
