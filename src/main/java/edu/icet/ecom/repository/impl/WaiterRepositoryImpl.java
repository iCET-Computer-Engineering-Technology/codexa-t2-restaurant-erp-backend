package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.OrderAssign;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.repository.WaiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaiterRepositoryImpl implements WaiterRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void assignWaiter(Long orderId, Long waiterId) {
            String sql = "INSERT INTO order_assignments (order_id, waiter_id, status) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, orderId, waiterId, "UNSERVED");
    }

    @Override
    public List<OrderAssign> getAssignments() {
        String sql = "SELECT * FROM order_assignments";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            OrderAssign assignment = new OrderAssign();

            assignment.setId(rs.getLong("id"));
            assignment.setOrderId(rs.getLong("order_id"));
            assignment.setWaiterId(rs.getLong("waiter_id"));
            assignment.setStatus(rs.getString("status"));

            return assignment;

        });
    }

    @Override
    public List<OrderAssign> getUnservedOrders(Long waiterId) {
        String sql =  "SELECT oa.id, oa.order_id, oa.waiter_id, oa.status, " +
                "o.table_id, o.order_number " +
                "FROM order_assignments oa " +
                "JOIN orders o ON oa.order_id = o.id " +
                "WHERE oa.status = 'UNSERVED' AND oa.waiter_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            OrderAssign assignment = new OrderAssign();

            assignment.setId(rs.getLong("id"));
            assignment.setOrderId(rs.getLong("order_id"));
            assignment.setWaiterId(rs.getLong("waiter_id"));
            assignment.setStatus(rs.getString("status"));
            assignment.setTableId(rs.getLong("table_id"));
            assignment.setOrderNumber(rs.getString("order_number"));
            return assignment;
        }, waiterId);
    }

    @Override
    public List<Waiter> findActiveWaiters() {
        String sql = "SELECT * FROM waiters WHERE status='ACTIVE'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            Waiter waiter = new Waiter();

            waiter.setId(rs.getLong("id"));
            waiter.setName(rs.getString("name"));
            waiter.setStatus(rs.getString("status"));

            return waiter;

        });
    }

    @Override
    public boolean markOrderServed(Long assigmentId) {
        String sql = "UPDATE order_assignments SET status='SERVED' WHERE id=?";
        return jdbcTemplate.update(sql,assigmentId ) > 0;
    }
}
