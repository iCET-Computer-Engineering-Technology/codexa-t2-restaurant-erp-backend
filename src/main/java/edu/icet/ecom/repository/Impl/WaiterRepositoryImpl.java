package edu.icet.ecom.repository.Impl;

import edu.icet.ecom.entity.OrderAssigment;
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

        String sql = """
                INSERT INTO order_assignments(order_id, waiter_id, status)
                VALUES (?,?, 'UNSERVED')
                """;

        jdbcTemplate.update(sql, orderId, waiterId);

    }

    @Override
    public List<OrderAssigment> getAssignments() {
        String sql = "SELECT * FROM order_assignments";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            OrderAssigment assignment = new OrderAssigment();

            assignment.setId(rs.getLong("id"));
            assignment.setOrderId(rs.getLong("order_id"));
            assignment.setWaiterId(rs.getLong("waiter_id"));
            assignment.setStatus(rs.getString("status"));

            return assignment;

        });
    }

    @Override
    public List<OrderAssigment> getUnservedOrders() {

        String sql = "SELECT * FROM order_assignments JOIN orders " +
                "ON order_assignments.order_id = orders.order_id " +
                "WHERE status='UNSERVED'";

        return jdbcTemplate.query(sql,(rs,rowNum)->{

            OrderAssigment order = new OrderAssigment();

            order.setOrderId(rs.getLong("order_id"));
            order.setWaiterId(rs.getLong("waiter_id"));
            order.setTableId(rs.getLong("table_no"));
            order.setStatus(rs.getString("status"));

            return order;
        });
    }
}

