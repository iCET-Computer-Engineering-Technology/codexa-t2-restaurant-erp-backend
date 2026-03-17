package edu.icet.ecom.repository.impl;
import edu.icet.ecom.entity.OrderAssign;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.repository.WaiterRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class WaiterRepositoryImpl implements WaiterRepository {
    private static final String COL_ID = "id";
    private static final String COL_ORDER_ID = "order_id";
    private static final String COL_WAITER_ID = "waiter_id";
    private static final String COL_STATUS = "status";
    private static final String COL_TABLE_ID = "table_id";
    private static final String COL_ORDER_NUMBER = "order_number";
    private static final String COL_NAME = "name";
    private static final String COL_ASSIGNED_AT = "assigned_at";

    private static final String STATUS_UNSERVED = "UNSERVED";
    private static final String STATUS_SERVED = "SERVED";
    private static final String STATUS_ACTIVE = "ACTIVE";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void assignWaiter(Long orderId, Long waiterId) {
        String sql = "INSERT INTO order_assignments (order_id, waiter_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, orderId, waiterId, STATUS_UNSERVED);
    }

    @Override
    public List<OrderAssign> getUnservedOrders(Long waiterId) {
        String sql = "SELECT oa.id, oa.order_id, oa.waiter_id, oa.status, " +
                "o.table_id, o.order_number " +
                "FROM order_assignments oa " +
                "JOIN orders o ON oa.order_id = o.id " +
                "WHERE oa.status = ? AND oa.waiter_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapOrderAssign(rs), STATUS_UNSERVED, waiterId);
    }

    @Override
    public List<Waiter> findActiveWaiters() {
        String sql = "SELECT * FROM waiters WHERE status=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapWaiter(rs), STATUS_ACTIVE);
    }

    @Override
    public boolean markOrderServed(Long assignmentId) {
        String sql = "UPDATE order_assignments SET status=? WHERE id=?";
        return jdbcTemplate.update(sql, STATUS_SERVED, assignmentId) > 0;
    }

    private OrderAssign mapOrderAssign(ResultSet rs) throws SQLException {
        OrderAssign assignment = new OrderAssign();
        assignment.setId(rs.getLong(COL_ID));
        assignment.setOrderId(rs.getLong(COL_ORDER_ID));
        assignment.setWaiterId(rs.getLong(COL_WAITER_ID));
        assignment.setStatus(rs.getString(COL_STATUS));
        assignment.setTableId(rs.getLong(COL_TABLE_ID));
        assignment.setOrderNumber(rs.getString(COL_ORDER_NUMBER));
        Timestamp timestamp = rs.getTimestamp(COL_ASSIGNED_AT);
        if (timestamp != null) {
            assignment.setAssignedAt(timestamp.toLocalDateTime());
        }
        return assignment;
    }

    private Waiter mapWaiter(ResultSet rs) throws SQLException {
        Waiter waiter = new Waiter();
        waiter.setId(rs.getLong(COL_ID));
        waiter.setName(rs.getString(COL_NAME));
        waiter.setStatus(rs.getString(COL_STATUS));
        return waiter;
    }
}