package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.RecipeDto;
import edu.icet.ecom.dto.RecipeIngredientDto;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.repository.RecipeRepository;
import edu.icet.ecom.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public RecipeDto createRecipe(RecipeDto recipeDto) {
        Integer recipeId = recipeRepository.createRecipe(recipeDto);
        
        if (recipeId != null) {
            recipeDto.setId(recipeId);
        }

        if (recipeDto.getIngredients() != null) {
            for (RecipeIngredientDto ing : recipeDto.getIngredients()) {
                recipeRepository.saveRecipeIngredient(recipeDto.getId(), ing);
            }
        }
        
        return getRecipeByMenuItemId(recipeDto.getMenuItemId());
    }

    @Override
    public RecipeDto getRecipeByMenuItemId(Integer menuItemId) {
        RecipeDto recipe = recipeRepository.getRecipeByMenuItemId(menuItemId);
        
        if (recipe == null) {
            throw new ResourceNotFoundException("Recipe not found for Menu Item ID: " + menuItemId);
        }
        
        return recipe;
    }

    @Override
    @Transactional
    public RecipeDto updateRecipe(Integer id, RecipeDto recipeDto) {
        int count = recipeRepository.updateRecipeNotes(id, recipeDto.getNotes());
                
        if (count == 0) {
            throw new ResourceNotFoundException("Recipe not found ID: " + id);
        }
        
        // Simpler for updates: remove existing ingredients and re-add provided ones
        recipeRepository.deleteRecipeIngredients(id);
        
        if (recipeDto.getIngredients() != null) {
            for (RecipeIngredientDto ing : recipeDto.getIngredients()) {
                recipeRepository.saveRecipeIngredient(id, ing);
            }
        }
        
        Integer menuItemId = recipeRepository.getMenuItemIdByRecipeId(id);
                
        return getRecipeByMenuItemId(menuItemId);
    }

    @Override
    public void deleteRecipe(Integer id) {
        int count = recipeRepository.deleteRecipe(id);
        if (count == 0) {
            throw new ResourceNotFoundException("Recipe not found ID: " + id);
        }
    }

    @Override
    public void deleteIngredientFromRecipe(Integer recipeId, Integer ingredientId) {
        recipeRepository.deleteIngredientFromRecipe(recipeId, ingredientId);
    }
}
