package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.RecipeIngredient;
import edu.icet.ecom.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<RecipeIngredient> findCurrentRecipeIngredientsByMenuItemId(Integer menuItemId) {
        String sql = """
                SELECT ri.id, ri.recipe_id, ri.ingredient_id, ri.quantity, ri.unit
                FROM recipe_ingredients ri
                WHERE ri.recipe_id = (
                    SELECT r.id
                    FROM recipes r
                    WHERE r.menu_item_id = ?
                    ORDER BY r.is_current DESC, r.version_number DESC, r.created_at DESC, r.id DESC
                    LIMIT 1
                )
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setId(rs.getInt("id"));
            recipeIngredient.setRecipeId(rs.getInt("recipe_id"));
            recipeIngredient.setIngredientId(rs.getInt("ingredient_id"));
            recipeIngredient.setQuantity(rs.getBigDecimal("quantity"));
            recipeIngredient.setUnit(rs.getString("unit"));
            return recipeIngredient;
        }, menuItemId);
    }
}
