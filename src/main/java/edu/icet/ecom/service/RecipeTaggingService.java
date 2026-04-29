package edu.icet.ecom.service;

import edu.icet.ecom.dto.TaggingDto;

public interface RecipeTaggingService {
    boolean tagRecipeToMenuItem(Integer itemId, Integer recipeId);
    TaggingDto getRecipeByMenuItemId(Integer itemId);
}
