package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Recipe;
import edu.icet.ecom.repository.RecipeTaggingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecipeTaggingRepositoryImpl implements RecipeTaggingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean updateMenuItemRecipeId(Integer itemId, Integer recipeId) {

        String sql = "UPDATE menu_items SET recipe_id = ? WHERE id = ?";
        return jdbcTemplate.update(sql, recipeId, itemId) > 0;
    }

    @Override
    public Recipe getRecipeByMenuItemId(Integer itemId) {
        String sql = "SELECT r.* FROM recipes r JOIN menu_items m ON r.id = m.recipe_id WHERE m.id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Recipe recipe = new Recipe();
                recipe.setId(rs.getInt("id"));
                recipe.setNotes(rs.getString("notes"));
                recipe.setVersionNumber(rs.getInt("version_number"));
                recipe.setIsCurrent(rs.getInt("is_current"));
                return recipe;
            }, itemId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}