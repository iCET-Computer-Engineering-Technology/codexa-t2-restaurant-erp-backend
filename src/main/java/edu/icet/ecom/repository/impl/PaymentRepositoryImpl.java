package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addPayment(PaymentDto paymentDto) {
        try {
            if (paymentDto.getOrderId() == null ||
                    paymentDto.getPaymentMethod() == null ||
                    paymentDto.getAmount() == null) {
                throw new IllegalArgumentException(
                        "Order ID, payment method and amount are required"
                );
            }

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
    public List<PaymentDto> getAllPayment() {
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
            return false;
        }
    }
}
