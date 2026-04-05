package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.StockDiscrepancyDto;
import edu.icet.ecom.repository.StockCountSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockCountSessionRepositoryImpl implements StockCountSessionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<StockDiscrepancyDto> getDiscrepancyReport(Integer sessionId) {
        String sql = """
                SELECT s.id AS session_id,
                       s.session_date,
                       i.id AS ingredient_id,
                       i.name AS ingredient_name,
                       sci.system_quantity,
                       sci.counted_quantity,
                       sci.variance
                FROM stock_count_items sci
                INNER JOIN stock_count_sessions s ON s.id = sci.session_id
                INNER JOIN ingredients i ON i.id = sci.ingredient_id
                WHERE sci.variance <> 0
                  AND (? IS NULL OR sci.session_id = ?)
                ORDER BY s.session_date DESC, i.name ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            StockDiscrepancyDto dto = new StockDiscrepancyDto();
            dto.setSessionId(rs.getInt("session_id"));
            dto.setSessionDate(rs.getObject("session_date", java.time.LocalDate.class));
            dto.setIngredientId(rs.getInt("ingredient_id"));
            dto.setIngredientName(rs.getString("ingredient_name"));
            dto.setSystemQuantity(rs.getBigDecimal("system_quantity"));
            dto.setCountedQuantity(rs.getBigDecimal("counted_quantity"));
            dto.setVariance(rs.getBigDecimal("variance"));
            return dto;
        }, sessionId, sessionId);
    }
}

