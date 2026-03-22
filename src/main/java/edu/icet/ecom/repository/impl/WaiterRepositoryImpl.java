package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.OrderAssignment;
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
        String sql = """
                INSERT INTO order_assignment (kitchen_order_id, waiter_id)
                SELECT ko.id, ?
                FROM kitchen_order ko
                WHERE ko.order_id = ?
                ORDER BY ko.id DESC
                LIMIT 1
                """;
        jdbcTemplate.update(sql, waiterId, orderId);
    }

    @Override
    public List<OrderAssignment> getUnservedOrders(Long waiterId) {
        String sql = """
                SELECT oa.id, oa.kitchen_order_id, oa.waiter_id, oa.assigned_at
                FROM order_assignment oa
                JOIN kitchen_order ko ON ko.id = oa.kitchen_order_id
                LEFT JOIN order_status_updates osu
                ON osu.order_id = ko.order_id AND osu.waiter_id = oa.waiter_id
                WHERE oa.waiter_id = ?
                AND (osu.status IS NULL OR osu.status = 'unserved')
                ORDER BY oa.assigned_at DESC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    OrderAssignment assignment = new OrderAssignment();
                    assignment.setId(rs.getLong("id"));
                    assignment.setKitchenOrderId(rs.getLong("kitchen_order_id"));
                    assignment.setWaiterId(rs.getLong("waiter_id"));
                    assignment.setAssignedAt(
                            rs.getTimestamp("assigned_at") != null
                                    ? rs.getTimestamp("assigned_at").toLocalDateTime()
                                    : null
                    );
                    return assignment;
                },
                waiterId
        );
    }

    @Override
    public List<Waiter> findActiveWaiters() {
        String sql = "SELECT * FROM waiter WHERE status = 'active'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Waiter waiter = new Waiter();
            waiter.setId(rs.getLong("id"));
            waiter.setName(rs.getString("waiter_name"));
            waiter.setStatus(rs.getString("status"));
            return waiter;
        });
    }
}

