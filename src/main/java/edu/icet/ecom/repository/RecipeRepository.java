package edu.icet.ecom.repository;

import edu.icet.ecom.entity.RecipeIngredient;

import java.util.List;

public interface RecipeRepository {
    List<RecipeIngredient> findCurrentRecipeIngredientsByMenuItemId(Integer menuItemId);
}

