package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addPayment(PaymentDto paymentDto) {
        return jdbcTemplate.update("INSERT INTO payments (order_id , payment_method , amount , tip_amount , reference_number , processed_by , processed_at) VALUES (?,?,?,?,?,?,?)",
                paymentDto.getOrderId(),
                paymentDto.getPaymentMethod(),
                paymentDto.getAmount(),
                paymentDto.getTipAmount(),
                paymentDto.getReferenceNumber(),
                paymentDto.getProcessedBy(),
                paymentDto.getProcessedAt()
        )>0;
    }

    @Override
    public List<PaymentDto> getAllPayment() {
        return jdbcTemplate.query("SELECT * FROM payments" , (rs, rowNum) -> new PaymentDto (
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getDouble(4),
                rs.getDouble(5),
                rs.getString(6),
                rs.getInt(7),
                rs.getTimestamp(8)
        ));
    }
}
