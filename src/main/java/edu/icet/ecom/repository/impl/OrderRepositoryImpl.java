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

    //Geeth
    @Override
    public List<Order> findReceivedOrders() {
        String sql = "SELECT id, table_id, customer_id, order_number, status, total_amount, tax, payment_status, created_at, updated_at " +
                "FROM orders WHERE status = 'RECEIVED' " +
                "ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) ->{
            Order order = new Order();
            order.setId(rs.getLong(1));
            order.setTableId(rs.getLong(2));
            if (!rs.wasNull()) {
                order.setCustomerId(rs.getLong(3));
            } else {
                order.setCustomerId(null);
            }
            order.setOrderNumber(rs.getString(4));
            order.setStatus(rs.getString(5));
            order.setTotalAmount(rs.getBigDecimal(6));
            order.setTax(rs.getBigDecimal(7));
            order.setPaymentStatus(rs.getString(8));
            order.setCreatedAt(rs.getTimestamp(9).toLocalDateTime());
            order.setUpdatedAt(rs.getTimestamp(10).toLocalDateTime());
            return order;
        });
    }

    //Geeth
    @Override
    public boolean updateStatus(Long orderId, String status) {
        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, status, orderId)>0;
    }

    //Amila
    @Override
    public int upsertAndGetSequence(LocalDate date) {
        jdbcTemplate.update(
                "INSERT INTO order_sequence (sequence_date, last_sequence) VALUES (?, 1) " +
                        "ON DUPLICATE KEY UPDATE last_sequence = LAST_INSERT_ID(last_sequence + 1)", date);
        Integer sequence = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        if (sequence == null) {
            throw new DataRetrievalFailureException("Failed to get sequence: LAST_INSERT_ID() returned null");
        }
        return sequence;
    }

    //Amila
    @Override
    public Long saveAndGetId(Order order) {
        String sql = "INSERT INTO orders (table_id, customer_id, order_number, status, total_amount, tax, payment_status, created_at, updated_at) "+
                    "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        KeyHolder keyHolder =  new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.getTableId());
            if(order.getCustomerId() != null) {
                ps.setLong(2, order.getCustomerId());
            }else {
                ps.setNull(2, java.sql.Types.BIGINT);
            }
            ps.setString(3, order.getOrderNumber());
            ps.setString(4, order.getStatus());
            ps.setBigDecimal(5, order.getTotalAmount());
            ps.setBigDecimal(6, order.getTax());
            ps.setString(7, order.getPaymentStatus());
            return ps;
            }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey())
                    .map(Number::longValue)
                    .orElseThrow(() -> new DataRetrievalFailureException("Order insert failed: no generated key returned"));
    }
}
