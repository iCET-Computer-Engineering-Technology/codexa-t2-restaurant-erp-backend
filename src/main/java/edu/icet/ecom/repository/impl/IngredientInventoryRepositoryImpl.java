package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Ingredient;
import edu.icet.ecom.repository.IngredientInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientInventoryRepositoryImpl implements IngredientInventoryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Ingredient> findByIdsForUpdate(List<Integer> ingredientIds) {
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return List.of();
        }

        String placeholders = String.join(",", Collections.nCopies(ingredientIds.size(), "?"));
        String sql = "SELECT id, name, unit, current_stock, low_stock_threshold, cost_per_unit FROM ingredients WHERE id IN (" + placeholders + ") FOR UPDATE";

        return jdbcTemplate.query(sql, ingredientIds.toArray(), (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getInt("id"));
            ingredient.setName(rs.getString("name"));
            ingredient.setUnit(rs.getString("unit"));
            ingredient.setCurrentStock(rs.getBigDecimal("current_stock"));
            ingredient.setLowStockThreshold(rs.getBigDecimal("low_stock_threshold"));
            ingredient.setCostPerUnit(rs.getBigDecimal("cost_per_unit"));
            return ingredient;
        });
    }

    @Override
    public boolean deductStock(Integer ingredientId, BigDecimal quantity) {
        String sql = "UPDATE ingredients SET current_stock = current_stock - ?, updated_at = NOW() WHERE id = ? AND current_stock >= ?";
        return jdbcTemplate.update(sql, quantity, ingredientId, quantity) > 0;
    }

    @Override
    public BigDecimal findCurrentStock(Integer ingredientId) {
        String sql = "SELECT current_stock FROM ingredients WHERE id = ?";
        BigDecimal stock = jdbcTemplate.queryForObject(sql, BigDecimal.class, ingredientId);
        return stock == null ? BigDecimal.ZERO : stock;
    }
}

