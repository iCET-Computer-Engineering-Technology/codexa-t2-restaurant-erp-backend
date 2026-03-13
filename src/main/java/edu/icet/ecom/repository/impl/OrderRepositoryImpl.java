package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.OrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> findReceivedOrders() {
        String sql = "SELECT id,table_id,order_number,status FROM orders WHERE status='RECEIVED'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setTableId(rs.getLong("table_id"));
            order.setOrderNumber(rs.getString("order_number"));
            order.setStatus(rs.getString("status"));

            return order;
        });
    }

    @Override
    public void updateStatus(Long orderId, String status) {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        jdbcTemplate.update(sql, status, orderId);
    }
}
