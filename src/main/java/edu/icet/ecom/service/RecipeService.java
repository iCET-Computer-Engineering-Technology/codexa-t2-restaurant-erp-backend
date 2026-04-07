package edu.icet.ecom.service;

import edu.icet.ecom.dto.RecipeDto;

public interface RecipeService {
    RecipeDto createRecipe(RecipeDto recipeDto);
    RecipeDto getRecipeByMenuItemId(Integer menuItemId);
    RecipeDto updateRecipe(Integer id, RecipeDto recipeDto);
    void deleteRecipe(Integer id);
    void deleteIngredientFromRecipe(Integer recipeId, Integer ingredientId);
}

