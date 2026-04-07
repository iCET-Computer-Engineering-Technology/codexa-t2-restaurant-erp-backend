package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.IngredientForSupplierDto;
import edu.icet.ecom.dto.SupplierWithIngredientsDto;
import edu.icet.ecom.repository.SupplierIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SupplierIngredientRepositoryImpl implements SupplierIngredientRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<IngredientForSupplierDto> ingredientMapper = (rs, rowNum) ->
            new IngredientForSupplierDto(
                    rs.getLong("ingredient_id"),
                    rs.getString("ingredient_name"),
                    rs.getString("supplier_sku"),
                    rs.getBigDecimal("unit_price"),
                    rs.getBigDecimal("min_order_qty"),
                    rs.getObject("price_date", java.time.LocalDate.class)
            );

    @Override
    public List<SupplierWithIngredientsDto> findAllSuppliersWithIngredients() {
        String sql = """
            SELECT s.id AS supplier_id,
                   s.name AS supplier_name,
                   si.ingredient_id,
                   i.name AS ingredient_name,
                   si.supplier_sku,
                   si.unit_price,
                   si.min_order_qty,
                   si.price_date
            FROM suppliers s
            LEFT JOIN supplier_ingredients si ON s.id = si.supplier_id
            LEFT JOIN ingredients i ON si.ingredient_id = i.id
            ORDER BY s.name, i.name
            """;

        return jdbcTemplate.query(sql, rs -> {
            List<SupplierWithIngredientsDto> result = new ArrayList<>();
            Long currentSupplierId = null;
            SupplierWithIngredientsDto currentSupplier = null;

            while (rs.next()) {
                Long supplierId = rs.getLong("supplier_id");

                if (currentSupplierId == null || !currentSupplierId.equals(supplierId)) {
                    currentSupplierId = supplierId;
                    currentSupplier = new SupplierWithIngredientsDto();
                    currentSupplier.setSupplierId(supplierId);
                    currentSupplier.setSupplierName(rs.getString("supplier_name"));
                    currentSupplier.setIngredients(new ArrayList<>());
                    result.add(currentSupplier);
                }

                if (rs.getObject("ingredient_id") != null) {
                    IngredientForSupplierDto ing = ingredientMapper.mapRow(rs, rs.getRow());
                    currentSupplier.getIngredients().add(ing);
                }
            }

            return result;
        });
    }

    @Override
    public List<IngredientForSupplierDto> findIngredientsBySupplierId(Long supplierId) {
        String sql = """
            SELECT si.ingredient_id,
                   i.name AS ingredient_name,
                   si.supplier_sku,
                   si.unit_price,
                   si.min_order_qty,
                   si.price_date
            FROM supplier_ingredients si
            JOIN ingredients i ON si.ingredient_id = i.id
            WHERE si.supplier_id = ?
            ORDER BY i.name
            """;

        return jdbcTemplate.query(sql, ingredientMapper, supplierId);
    }
}