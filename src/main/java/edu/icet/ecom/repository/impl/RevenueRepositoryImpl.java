package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.ReconciliationReportDto;
import edu.icet.ecom.dto.RevenueSummaryDto;
import edu.icet.ecom.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RevenueRepositoryImpl implements RevenueRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<RevenueSummaryDto> getRevenueByChannel(LocalDate date) {
        String sql = "SELECT channel, SUM(amount) as total FROM transactions WHERE date = ? GROUP BY channel";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new RevenueSummaryDto(
                rs.getString("channel"),
                rs.getDouble("total"),
                date
        ), date);
    }

    @Override
    public List<ReconciliationReportDto> getDailyReconciliation(LocalDate date) {
        String sql = """
                    SELECT 
                        p.sale_date, 
                        SUM(p.pos_amount) AS total_pos, 
                        COALESCE(SUM(r.received_amount), 0) AS total_actual,
                        (SUM(p.pos_amount) - COALESCE(SUM(r.received_amount), 0)) AS discrepancy
                    FROM pos_transactions p
                    LEFT JOIN revenue_records r ON p.transaction_id = r.transaction_id
                    WHERE p.sale_date = ?
                    GROUP BY p.sale_date
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            double discrepancy = rs.getDouble("discrepancy");
            String status = (discrepancy == 0) ? "Matched" : "Flagged";

            return new ReconciliationReportDto(
                    rs.getDate("sale_date").toLocalDate(),
                    rs.getDouble("total_pos"),
                    rs.getDouble("total_actual"),
                    discrepancy,
                    status
            );
        }, date);
    }
}
