package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.RevenueResponseDto;
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
    public List<RevenueResponseDto> getRevenueByChannel(LocalDate date) {
        String sql = "SELECT COALESCE(ot.type_name, 'unknown') AS channelType, SUM(p.amount) AS totalRevenue " +
                "FROM orders o " +
                "JOIN payments p ON o.id = p.order_id " +
                "LEFT JOIN order_types ot ON o.order_type_id = ot.id " +
                "WHERE DATE(p.processed_at) = ? " +
                "GROUP BY COALESCE(ot.type_name, 'unknown') " +
                "ORDER BY channelType";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new RevenueResponseDto(
                        rs.getString("channelType"),
                        rs.getDouble("totalRevenue")
                ), date);
    }
}