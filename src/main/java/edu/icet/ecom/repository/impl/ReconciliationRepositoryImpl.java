package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.ReconciliationDto;
import edu.icet.ecom.repository.ReconciliationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class ReconciliationRepositoryImpl implements ReconciliationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Double getTotalOrderAmountForDate(LocalDate date) {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE DATE(created_at) = ?";
        Double total = jdbcTemplate.queryForObject(sql, Double.class, date);
        return total != null ? total : 0.0;
    }

    @Override
    public Double getTotalPaymentAmountForDate(LocalDate date) {
        String sql = "SELECT SUM(amount) FROM payments WHERE DATE(processed_at) = ?";
        Double total = jdbcTemplate.queryForObject(sql, Double.class, date);
        return total != null ? total : 0.0;
    }

    @Override
    public void saveReconciliation(ReconciliationDto dto) {
        String sql = "INSERT INTO reconciliation_logs (recon_date, total_order_amount, total_payment_amount, discrepancy, status) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE total_order_amount = VALUES(total_order_amount), " +
                "total_payment_amount = VALUES(total_payment_amount), discrepancy = VALUES(discrepancy), status = VALUES(status)";
        jdbcTemplate.update(sql, dto.getReconciliationDate(), dto.getTotalOrderAmount(), dto.getTotalPaymentAmount(), dto.getDiscrepancy(), dto.getStatus());
    }
}
