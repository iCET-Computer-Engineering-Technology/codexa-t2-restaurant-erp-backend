package edu.icet.ecom.repository.impl;

import edu.icet.ecom.repository.OrderAssignmentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
