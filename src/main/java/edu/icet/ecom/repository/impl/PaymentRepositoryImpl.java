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
                paymentDto.getAmount(),
                paymentDto.getTipAmount(),
                paymentDto.getTipAmount(),
                paymentDto.getReferenceNumber(),
                paymentDto.getProcessedBy(),
                paymentDto.getProcessedAt()
        )>0;
    }

    @Override
    public List<PaymentDto> getAllPayment() {
        return List.of();
    }
}
