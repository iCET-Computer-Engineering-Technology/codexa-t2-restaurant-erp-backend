package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.WaiterOrder;
import edu.icet.ecom.entity.Waiters;
import edu.icet.ecom.repository.WaitersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class WaitersRepositoryImpl implements WaitersRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Waiters> getActiveWaiters() {
        String sql = "SELECT id, full_name, email, role, is_active, created_at " +
                "FROM users WHERE role = 'WAITER' AND is_active = 1";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Waiters waiter = new Waiters();
            waiter.setId(rs.getInt("id"));
            waiter.setFullName(rs.getString("full_name"));
            waiter.setEmail(rs.getString("email"));
            waiter.setRole(rs.getString("role"));
            waiter.setIsActive(rs.getBoolean("is_active"));
            return waiter;
        });
    }

    @Override
    public List<WaiterOrder> getUnservedOrders(Integer waiterId) {
        String sql = "SELECT id, order_number, order_type, table_id, customer_id, " +
                "server_id, status, total_amount, created_at " +
                "FROM orders " +
                "WHERE server_id = ? AND status IN ('open', 'sent_to_kitchen', 'partially_ready', 'ready')";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            WaiterOrder order = new WaiterOrder();
            order.setId(rs.getInt("id"));
            order.setOrderNumber(rs.getString("order_number"));
            order.setOrderType(rs.getString("order_type"));
            order.setTableId(rs.getInt("table_id"));
            order.setCustomerId(rs.getInt("customer_id"));
            order.setServerId(rs.getInt("server_id"));
            order.setStatus(rs.getString("status"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            return order;
        }, waiterId);
    }

    @Override
    public boolean serveOrder(Integer orderId) {
        String sql = "UPDATE orders SET status = 'paid' WHERE id = ?";
        return jdbcTemplate.update(sql, orderId) > 0;
    }
}
