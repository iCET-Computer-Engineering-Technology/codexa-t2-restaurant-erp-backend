package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.TaggingDto;
import edu.icet.ecom.entity.Recipe;
import edu.icet.ecom.repository.RecipeTaggingRepository;
import edu.icet.ecom.service.RecipeTaggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class RecipeTaggingServiceImpl implements RecipeTaggingService {
    private final RecipeTaggingRepository recipeTaggingRepository;

    @Override
    public boolean tagRecipeToMenuItem(Integer itemId, Integer recipeId) {
        return recipeTaggingRepository.updateMenuItemRecipeId(itemId, recipeId);
    }

    @Override
    public TaggingDto getRecipeByMenuItemId(Integer itemId) {
        TaggingDto recipe = recipeTaggingRepository.getRecipeByMenuItemId(itemId);

        if (recipe == null) {
            return null;
        }

        TaggingDto dto = new TaggingDto();
        dto.setId(recipe.getId());
        dto.setNotes(recipe.getNotes());

        return dto;
    }
}
