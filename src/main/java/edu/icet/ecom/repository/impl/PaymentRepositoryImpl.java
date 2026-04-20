package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.List;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addPayment(PaymentDto paymentDto) {
        try {
            int rows = jdbcTemplate.update(
                    "INSERT INTO payments (order_id , payment_method , amount , " +
                            " tip_amount , reference_number , processed_by , processed_at) " +
                            " VALUES (?,?,?,?,?,?,?)",
                    paymentDto.getOrderId(),
                    paymentDto.getPaymentMethod(),
                    paymentDto.getAmount(),
                    paymentDto.getTipAmount() != null ? paymentDto.getTipAmount() : 0.00,
                    paymentDto.getReferenceNumber(),
                    paymentDto.getProcessedBy(),
                    paymentDto.getProcessedAt() != null
                            ? paymentDto.getProcessedAt()
                            : new Timestamp(System.currentTimeMillis())
            );
            return rows > 0;
        }catch(IllegalArgumentException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException("Failed to save payment : "+ e.getMessage());
        }
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM payments",
                    (rs, rowNum) -> new PaymentDto(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getString("payment_method"),
                            rs.getDouble("amount"),
                            rs.getDouble("tip_amount"),
                            rs.getString("reference_number"),
                            rs.getInt("processed_by"),
                            rs.getTimestamp("processed_at")
                    ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save payment : "+ e.getMessage());
        }
    }

    @Override
    public PaymentDto getPaymentByOrderId(Integer orderId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM payments WHERE order_id = ?",
                    (rs, rowNum) -> new PaymentDto(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getString("payment_method"),
                            rs.getDouble("amount"),
                            rs.getDouble("tip_amount"),
                            rs.getString("reference_number"),
                            rs.getInt("processed_by"),
                            rs.getTimestamp("processed_at")
                    ),
                    orderId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch payment: "
                    + e.getMessage());
        }
    }

    @Override
    public boolean updateOrderStatus(Integer orderId) {
        try {
            int rows = jdbcTemplate.update(
                    "UPDATE orders SET status = 'paid' WHERE id = ?",
                    orderId
            );
            return rows > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order status: "
                    + e.getMessage());
        }
    }

    @Override
    public boolean checkOrderExists(Integer orderId) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM orders WHERE id = ?",
                    Integer.class,
                    orderId
            );
            return count != null && count > 0;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to check order existence: " + e.getMessage()
            );
        }
    }
    @Override
    public Map<String, Object> findPaymentWithUserByOrderId(Integer orderId) {
        String sql = """
            SELECT p.id, p.order_id, p.payment_method, p.amount, 
                   p.tip_amount, p.reference_number, 
                   u.username as processed_by_username,
                   p.processed_at
            FROM payments p
            LEFT JOIN users u ON p.processed_by = u.id
            WHERE p.order_id = ?
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Map<String, Object> result = new HashMap<>();
                result.put("id", rs.getInt("id"));
                result.put("order_id", rs.getInt("order_id"));
                result.put("payment_method", rs.getString("payment_method"));
                result.put("amount", rs.getBigDecimal("amount"));
                result.put("tip_amount", rs.getBigDecimal("tip_amount"));
                result.put("reference_number", rs.getString("reference_number"));
                result.put("processed_by_username", rs.getString("processed_by_username"));

                Timestamp processedAt = rs.getTimestamp("processed_at");
                result.put("processed_at", processedAt != null ? processedAt.toLocalDateTime() : null);

                return result;
            }, orderId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> findPaymentsByOrderIds(List<Integer> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(orderIds.size(), "?"));
        String sql = String.format("""
            SELECT p.id, p.order_id, p.payment_method, p.amount,
                   p.tip_amount, p.reference_number,
                   u.username as processed_by_username,
                   p.processed_at
            FROM payments p
            LEFT JOIN users u ON p.processed_by = u.id
            WHERE p.order_id IN (%s)
            """, placeholders);

        return jdbcTemplate.query(sql, orderIds.toArray(), (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", rs.getInt("id"));
            result.put("order_id", rs.getInt("order_id"));
            result.put("payment_method", rs.getString("payment_method"));
            result.put("amount", rs.getBigDecimal("amount"));
            result.put("tip_amount", rs.getBigDecimal("tip_amount"));
            result.put("reference_number", rs.getString("reference_number"));
            result.put("processed_by_username", rs.getString("processed_by_username"));

            Timestamp processedAt = rs.getTimestamp("processed_at");
            result.put("processed_at", processedAt != null ? processedAt.toLocalDateTime() : null);

            return result;
        });
    }
}
