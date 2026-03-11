package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.OrderAssignment;
import edu.icet.ecom.repository.OrderAssignmentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderAssignmentRepositoryImpl implements OrderAssignmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderAssignmentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void assignWaiter(Long orderId, Long waiterId) {
        String sql = """
                INSERT INTO order_assignments(order_id, waiter_id)
                VALUES (?,?)
                """;

        jdbcTemplate.update(sql, orderId, waiterId);
    }

    @Override
    public List<OrderAssignment> getAssignments() {
        String sql = "SELECT * FROM order_assignments";

        return jdbcTemplate.query(sql,(rs,rowNum)->{

            OrderAssignment assignment = new OrderAssignment();

            assignment.setId(rs.getLong("id"));
            assignment.setOrderId(rs.getLong("order_id"));
            assignment.setWaiterId(rs.getLong("waiter_id"));

            return assignment;

        });
    }
}
