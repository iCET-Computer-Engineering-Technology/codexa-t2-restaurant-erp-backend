package edu.icet.ecom.repository;

import edu.icet.ecom.dto.RecipeDto;
import edu.icet.ecom.dto.RecipeIngredientDto;
import edu.icet.ecom.entity.RecipeIngredient;

import java.util.List;

public interface RecipeRepository {
    Integer createRecipe(RecipeDto recipeDto);
    RecipeDto getRecipeByMenuItemId(Integer menuItemId);
    int updateRecipeNotes(Integer id, String notes);
    void deleteRecipeIngredients(Integer recipeId);
    Integer getMenuItemIdByRecipeId(Integer id);
    int deleteRecipe(Integer id);
    void deleteIngredientFromRecipe(Integer recipeId, Integer ingredientId);
    void saveRecipeIngredient(Integer recipeId, RecipeIngredientDto ing);
    List<RecipeIngredient> findCurrentRecipeIngredientsByMenuItemId(Integer menuItemId);
}

