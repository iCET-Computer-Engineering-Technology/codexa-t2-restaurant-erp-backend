package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Integer saveAndGetId(Order order) {
        String sql ="INSERT INTO orders (order_number, order_type, table_id, customer_id, server_id, status, subtotal, discount_amount, " +
                "tax_amount, service_charge, total_amount, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection ->{
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getOrderNumber());
            ps.setString(2, order.getOrderType());
            if(order.getTableId() != null){
                ps.setInt(3, order.getTableId());
            }else{
                ps.setNull(3, Types.INTEGER);
            }
            if(order.getCustomerId() != null) {
                ps.setInt(4, order.getCustomerId());
            }else{
                ps.setNull(4, Types.INTEGER);
            }
            if(order.getServerId() != null){
                ps.setInt(5, order.getServerId());
            }else{
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, order.getStatus());
            ps.setBigDecimal(7, order.getSubTotal());
            ps.setBigDecimal(8, order.getDiscountAmount());
            ps.setBigDecimal(9, order.getTaxAmount());
            ps.setBigDecimal(10, order.getServiceCharge());
            ps.setBigDecimal(11, order.getTotalAmount());
            ps.setString(12, order.getNotes());
            return ps;
        }, keyHolder );
        return Optional.ofNullable(keyHolder.getKey())
                .map(Number::intValue).orElseThrow(()-> new DataRetrievalFailureException("Order insert failed - no generated key returned"));
    }

    @Override
    public boolean updateStatus(Integer orderId, String status) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Invalid orderId: " + orderId);
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        int rowsUpdated = jdbcTemplate.update(
                "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?",
                status, orderId
        );
        return rowsUpdated > 0;
    }

    @Override
    public Order findById(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, order_number, order_type, table_id, customer_id, server_id, " +
                            "status, subtotal, discount_amount, tax_amount, service_charge, " +
                            "total_amount, notes, created_at, updated_at " +
                            "FROM orders WHERE id = ?",
                    (rs, row) -> mapRow(rs), id
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;  // Order not found
        }
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(
                "SELECT id, order_number, order_type, table_id, customer_id, server_id, " +
                        "status, subtotal, discount_amount, tax_amount, service_charge, " +
                        "total_amount, notes, created_at, updated_at " +
                        "FROM orders ORDER BY created_at DESC",
                (rs, row) -> mapRow(rs)
        );
    }

    @Override
    public List<Order> findByStatus(String status) {
        return jdbcTemplate.query(
                "SELECT id, order_number, order_type, table_id, customer_id, server_id, " +
                        "status, subtotal, discount_amount, tax_amount, service_charge, " +
                        "total_amount, notes, created_at, updated_at " +
                        "FROM orders WHERE status = ? ORDER BY created_at DESC",
                (rs, row) -> mapRow(rs), status
        );
    }

    //Get sequence for order number - uses order_sequence table to maintain daily counter
    @Override
    public int upsertAndGetSequence(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        String sql = "INSERT INTO order_sequence (sequence_date, last_sequence) VALUES (?, 1) " +
                "ON DUPLICATE KEY UPDATE last_sequence = last_sequence + 1";
        jdbcTemplate.update(sql, sqlDate);

        String selectSql = "SELECT last_sequence FROM order_sequence WHERE sequence_date = ?";
        Integer sequence = jdbcTemplate.queryForObject(selectSql, Integer.class, sqlDate);
        return sequence != null ? sequence : 1;
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
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

        //Handle null timestamps to prevent NullPointerException
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
