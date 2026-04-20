package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReceiptRepositoryImpl implements ReceiptRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Order> findAllPaidOrders() {
        String sql = """
            SELECT o.id, o.order_type_id, o.order_number, ot.type_name AS order_type,
                   o.table_id, o.customer_id, o.server_id,
                   o.status, o.subtotal, o.discount_amount, o.tax_amount, o.service_charge,
                   o.total_amount, o.notes, o.created_at, o.updated_at
            FROM orders o
            LEFT JOIN order_types ot ON o.order_type_id = ot.id
            WHERE o.status = 'paid'
            ORDER BY o.created_at DESC
            """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapOrderRow(rs));
    }

    @Override
    public Order findPaidOrderById(Integer orderId) {
        String sql = """
            SELECT o.id, o.order_type_id, o.order_number, ot.type_name AS order_type,
                   o.table_id, o.customer_id, o.server_id,
                   o.status, o.subtotal, o.discount_amount, o.tax_amount, o.service_charge,
                   o.total_amount, o.notes, o.created_at, o.updated_at
            FROM orders o
            LEFT JOIN order_types ot ON o.order_type_id = ot.id
            WHERE o.id = ? AND o.status = 'paid'
            """;
        
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapOrderRow(rs), orderId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Order> searchPaidOrders(String orderNumber, LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT o.id, o.order_type_id, o.order_number, ot.type_name AS order_type,
                   o.table_id, o.customer_id, o.server_id,
                   o.status, o.subtotal, o.discount_amount, o.tax_amount, o.service_charge,
                   o.total_amount, o.notes, o.created_at, o.updated_at
            FROM orders o
            LEFT JOIN order_types ot ON o.order_type_id = ot.id
            WHERE o.status = 'paid'
            AND (? IS NULL OR o.order_number LIKE CONCAT('%', ?, '%'))
            AND (? IS NULL OR DATE(o.created_at) >= ?)
            AND (? IS NULL OR DATE(o.created_at) <= ?)
            ORDER BY o.created_at DESC
            """;
        
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> mapOrderRow(rs),
                orderNumber, orderNumber,
                startDate, startDate,
                endDate, endDate
        );
    }

    private Order mapOrderRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));

        int orderTypeId = rs.getInt("order_type_id");
        order.setOrderTypeId(rs.wasNull() ? null : orderTypeId);

        order.setOrderNumber(rs.getString("order_number"));
        order.setOrderType(rs.getString("order_type"));

        int tableId = rs.getInt("table_id");
        order.setTableId(rs.wasNull() ? null : tableId);

        int customerId = rs.getInt("customer_id");
        order.setCustomerId(rs.wasNull() ? null : customerId);

        int serverId = rs.getInt("server_id");
        order.setServerId(rs.wasNull() ? null : serverId);

        order.setStatus(rs.getString("status"));
        order.setSubTotal(rs.getBigDecimal("subtotal"));
        order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        order.setTaxAmount(rs.getBigDecimal("tax_amount"));
        order.setServiceCharge(rs.getBigDecimal("service_charge"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setNotes(rs.getString("notes"));

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            order.setCreatedAt(createdTs.toLocalDateTime());
        }

        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            order.setUpdatedAt(updatedTs.toLocalDateTime());
        }

        return order;
    }
}
