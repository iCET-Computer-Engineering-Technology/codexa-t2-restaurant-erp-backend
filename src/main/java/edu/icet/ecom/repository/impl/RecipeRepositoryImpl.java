package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.RecipeDto;
import edu.icet.ecom.dto.RecipeIngredientDto;
import edu.icet.ecom.entity.RecipeIngredient;
import edu.icet.ecom.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer createRecipe(RecipeDto recipeDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO recipes(menu_item_id, version_number, is_current, notes) VALUES (?, 1, 1, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, recipeDto.getMenuItemId(), java.sql.Types.INTEGER);
            ps.setString(2, recipeDto.getNotes());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : null;
    }

    @Override
    public RecipeDto getRecipeByMenuItemId(Integer menuItemId) {
        String recipeSql = "SELECT id, menu_item_id, version_number, is_current, notes FROM recipes " +
                           "WHERE menu_item_id = ? AND is_current = 1 ORDER BY id DESC LIMIT 1";

        List<Map<String, Object>> recipes = jdbcTemplate.queryForList(recipeSql, menuItemId);

        if (recipes.isEmpty()) {
            return null;
        }

        Map<String, Object> recipeRow = recipes.get(0);
        RecipeDto recipe = new RecipeDto();
        recipe.setId((Integer) recipeRow.get("id"));
        recipe.setMenuItemId((Integer) recipeRow.get("menu_item_id"));
        recipe.setVersionNumber((Integer) recipeRow.get("version_number"));
        recipe.setIsCurrent(((Number) recipeRow.get("is_current")).intValue() == 1);
        recipe.setNotes((String) recipeRow.get("notes"));

        String ingredientsSql = "SELECT ri.id, ri.ingredient_id, i.name as ingredient_name, ri.quantity, ri.unit " +
                                "FROM recipe_ingredients ri " +
                                "JOIN ingredients i ON ri.ingredient_id = i.id " +
                                "WHERE ri.recipe_id = ?";

        List<RecipeIngredientDto> ingredients = jdbcTemplate.query(ingredientsSql, (rs, rowNum) -> {
            RecipeIngredientDto dto = new RecipeIngredientDto();
            dto.setId(rs.getInt("id"));
            dto.setIngredientId(rs.getInt("ingredient_id"));
            dto.setIngredientName(rs.getString("ingredient_name"));
            dto.setQuantity(rs.getBigDecimal("quantity"));
            dto.setUnit(rs.getString("unit"));
            return dto;
        }, recipe.getId());

        recipe.setIngredients(ingredients);
        return recipe;
    }

    @Override
    public int updateRecipeNotes(Integer id, String notes) {
        return jdbcTemplate.update(
                "UPDATE recipes SET notes = ? WHERE id = ?",
                notes, id);
    }

    @Override
    public void deleteRecipeIngredients(Integer recipeId) {
        jdbcTemplate.update("DELETE FROM recipe_ingredients WHERE recipe_id = ?", recipeId);
    }

    @Override
    public Integer getMenuItemIdByRecipeId(Integer id) {
        return jdbcTemplate.queryForObject(
                "SELECT menu_item_id FROM recipes WHERE id = ?", Integer.class, id);
    }

    @Override
    public int deleteRecipe(Integer id) {
        return jdbcTemplate.update("DELETE FROM recipes WHERE id = ?", id);
    }

    @Override
    public void deleteIngredientFromRecipe(Integer recipeId, Integer ingredientId) {
        jdbcTemplate.update(
                "DELETE FROM recipe_ingredients WHERE recipe_id = ? AND ingredient_id = ?",
                recipeId, ingredientId);
    }

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

    @Override
    public void saveRecipeIngredient(Integer recipeId, RecipeIngredientDto ing) {
        jdbcTemplate.update(
                "INSERT INTO recipe_ingredients(recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)",
                recipeId, ing.getIngredientId(), ing.getQuantity(), ing.getUnit());
    }
}
