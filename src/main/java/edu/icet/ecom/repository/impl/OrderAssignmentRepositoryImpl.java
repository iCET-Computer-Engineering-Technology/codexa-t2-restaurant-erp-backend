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
    public void assignWaiter(Long kitchenOrderId, Long waiterId) {
        String sql = """
        INSERT INTO order_assignment(kitchen_order_id, waiter_id)
        VALUES (?,?)
    """;
        jdbcTemplate.update(sql, kitchenOrderId, waiterId);
    }

    @Override
    public List<OrderAssignment> getAssignments() {
        String sql = "SELECT * FROM order_assignment";

        return jdbcTemplate.query(sql,(rs,rowNum)->{

            OrderAssignment assignment = new OrderAssignment();

            assignment.setId(rs.getLong("id"));
            assignment.setKitchenOrderId(rs.getLong("kitchen_order_id"));
            assignment.setWaiterId(rs.getLong("waiter_id"));
            return assignment;
        });
    }

    @Override
    public boolean existsByKitchenOrderId(Long kitchenOrderId) {
        String sql = "SELECT COUNT(*) FROM order_assignment WHERE kitchen_order_id=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, kitchenOrderId);
        return count != null && count > 0;
    }
}
