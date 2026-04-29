package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Recipe;

public interface RecipeTaggingRepository {
    boolean updateMenuItemRecipeId(Integer itemId, Integer recipeId);
    Recipe getRecipeByMenuItemId(Integer itemId);
}
