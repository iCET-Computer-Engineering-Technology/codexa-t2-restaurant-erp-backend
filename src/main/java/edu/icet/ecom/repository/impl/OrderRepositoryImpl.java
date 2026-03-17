package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    //Get open orders
    @Override
    public List<Order> findOpenOrders() {
        String sql = "SELECT id, order_number, order_type, table_id, customer_id, server_id, " +
                "status, subtotal, discount_amount, tax_amount, total_amount, notes, source, " +
                "created_at, updated_at " +
                "FROM orders WHERE status = 'open' ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) ->{
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setOrderNumber(rs.getString("order_number"));
            order.setOrderType(rs.getString("order_type"));
            order.setTableId(rs.getInt("table_id"));

            int customerId = rs.getInt("customer_id");
            order.setCustomerId(rs.wasNull() ? null : customerId);

            int serverId = rs.getInt("server_id");
            order.setServerId(rs.wasNull() ? null : serverId);

            order.setStatus(rs.getString("status"));
            order.setSubTotal(rs.getBigDecimal("subtotal"));
            order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
            order.setTaxAmount(rs.getBigDecimal("tax_amount"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            order.setNotes(rs.getString("notes"));
            order.setSource(rs.getString("source"));
            order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return order;
        });
    }

    //Update status
    @Override
    public boolean updateStatus(Integer orderId, String status) {
        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, status, orderId)>0;
    }

    //Get sequence for order number
    @Override
    public int upsertAndGetSequence(LocalDate date) {
        jdbcTemplate.update(
                "INSERT INTO order_sequence (sequence_date, last_sequence) VALUES (?, 1) " +
                        "ON DUPLICATE KEY UPDATE last_sequence = last_sequence + 1", date);
        Integer sequence = jdbcTemplate.queryForObject(
                "SELECT last_sequence FROM order_sequence WHERE sequence_date = ?",
                Integer.class, date);

        if (sequence == null) {
            throw new DataRetrievalFailureException(
                    "Failed to get sequence for date: " + date);
        }
        return sequence;
    }

    //Amila
    @Override
    public Integer saveAndGetId(Order order) {
        String sql = "INSERT INTO orders (order_number, order_type, table_id, customer_id, server_id, status, " +
                " subtotal, discount_amount, tax_amount, total_amount, notes, source, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        KeyHolder keyHolder =  new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getOrderNumber());
            ps.setString(2, order.getOrderType());
            ps.setInt(3, order.getTableId());
            if (order.getCustomerId() != null) {
                ps.setInt(4, order.getCustomerId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            if (order.getServerId() != null) {
                ps.setInt(5, order.getServerId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setString(6, order.getStatus());
            ps.setBigDecimal(7, order.getSubTotal());
            ps.setBigDecimal(8, order.getDiscountAmount());
            ps.setBigDecimal(9, order.getTaxAmount());
            ps.setBigDecimal(10, order.getTotalAmount());
            ps.setString(11, order.getNotes());
            ps.setString(12, order.getSource());
            return ps;
            }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey())
                    .map(Number::intValue)
                    .orElseThrow(() -> new DataRetrievalFailureException("Order insert failed: no generated key returned"));
    }
}
