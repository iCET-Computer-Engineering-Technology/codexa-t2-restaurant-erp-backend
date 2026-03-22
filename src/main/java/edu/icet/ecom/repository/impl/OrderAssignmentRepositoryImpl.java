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
                INSERT INTO order_assignment(kitchen_order_id, waiter_id)
                VALUES (?,?)
                """;

        jdbcTemplate.update(sql, kitchenOrderId, waiterId);
    }

    @Override
    public boolean existsByKitchenOrderId(Long kitchenOrderId) {
        String sql = "SELECT COUNT(*) FROM order_assignment WHERE kitchen_order_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, kitchenOrderId);
        return count != null && count > 0;
    }

    @Override
    public List<WaiterDetails> getAssignments() {
        String sql = """
                SELECT oa.id, oa.kitchen_order_id, oa.waiter_id, oa.assigned_at, w.waiter_name
                FROM order_assignment oa
                INNER JOIN waiter w ON oa.waiter_id = w.id
                ORDER BY oa.assigned_at DESC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            WaiterDetails waiterNameDisplay = new WaiterDetails();
            waiterNameDisplay.setId(rs.getInt("id"));
            waiterNameDisplay.setKitchenOrderId(rs.getInt("kitchen_order_id"));
            waiterNameDisplay.setWaiterId(rs.getInt("waiter_id"));
            waiterNameDisplay.setWaiterName(rs.getString("waiter_name"));
            waiterNameDisplay.setAssignedAt(
                    rs.getTimestamp("assigned_at") != null
                            ? rs.getTimestamp("assigned_at").toLocalDateTime()
                            : null
            );
            return waiterNameDisplay;
        });
    }
}