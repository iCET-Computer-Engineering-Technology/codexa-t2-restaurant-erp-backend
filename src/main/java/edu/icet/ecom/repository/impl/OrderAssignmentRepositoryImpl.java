package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.WaiterDetails;
import edu.icet.ecom.repository.OrderAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderAssignmentRepositoryImpl implements OrderAssignmentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void assignWaiter(Long kitchenOrderId, Long waiterId) {
        String sql = """
                INSERT INTO order_assignment (kitchen_order_id, waiter_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(sql, kitchenOrderId, waiterId);
    }

    @Override
    public List<WaiterDetails> getAssignments() {
        String sql = """
                SELECT order_assignment.id, kitchen_order_id, waiter_id, assigned_at,waiter.waiter_name
                FROM order_assignment
                INNER JOIN waiter ON order_assignment.waiter_id = waiter.id
                ORDER BY assigned_at DESC; 
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            WaiterDetails waiterNameDisplay = new WaiterDetails();
            waiterNameDisplay.setId(rs.getInt("id"));
            waiterNameDisplay.setKitchenOrderId(rs.getInt("kitchen_order_id"));
            waiterNameDisplay.setWaiterId(rs.getInt("waiter_id"));
            waiterNameDisplay.setWaiter_name(rs.getString("waiter_name"));
            waiterNameDisplay.setAssignedAt(
                    rs.getTimestamp("assigned_at") != null
                            ? rs.getTimestamp("assigned_at").toLocalDateTime()
                            : null
            );
            return waiterNameDisplay;
        });
    }
}