package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.LowStockAlertDto;
import edu.icet.ecom.repository.InventoryAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryAlertRepositoryImpl implements InventoryAlertRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean existsActiveLowStockAlert(Integer ingredientId) {
        String sql = "SELECT COUNT(*) FROM inventory_alerts WHERE ingredient_id = ? AND alert_type = 'low_stock' AND resolved_at IS NULL";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, ingredientId);
        return count != null && count > 0;
    }

    @Override
    public void createLowStockAlert(Integer ingredientId) {
        String sql = "INSERT INTO inventory_alerts (ingredient_id, alert_type, notified_via) VALUES (?, 'low_stock', 'in_app')";
        jdbcTemplate.update(sql, ingredientId);
    }

    @Override
    public void resolveLowStockAlerts(Integer ingredientId) {
        String sql = "UPDATE inventory_alerts SET resolved_at = NOW() WHERE ingredient_id = ? AND alert_type = 'low_stock' AND resolved_at IS NULL";
        jdbcTemplate.update(sql, ingredientId);
    }

    @Override
    public List<LowStockAlertDto> findActiveLowStockAlerts() {
        String sql = """
                SELECT i.id AS ingredient_id,
                       i.name,
                       i.current_stock,
                       i.low_stock_threshold,
                       i.unit
                FROM ingredients i
                WHERE i.low_stock_threshold IS NOT NULL
                  AND i.current_stock <= i.low_stock_threshold
                ORDER BY i.name ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LowStockAlertDto dto = new LowStockAlertDto();
            dto.setIngredientId(rs.getInt("ingredient_id"));
            dto.setIngredientName(rs.getString("name"));
            dto.setCurrentStock(rs.getBigDecimal("current_stock"));
            dto.setThreshold(rs.getBigDecimal("low_stock_threshold"));
            dto.setUnit(rs.getString("unit"));
            return dto;
        });
    }
}

