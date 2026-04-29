package edu.icet.ecom.controller;

import edu.icet.ecom.dto.TaggingDto;
import edu.icet.ecom.service.RecipeTaggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
@CrossOrigin
public class RecipeTaggingController {
    private final RecipeTaggingService recipeTaggingService;

    @PutMapping("/tag/{itemId}/{recipeId}")
    public boolean tagRecipe(@PathVariable Integer itemId, @PathVariable Integer recipeId) {
        return recipeTaggingService.tagRecipeToMenuItem(itemId, recipeId);
    }
    @GetMapping("/item/{itemId}")
    public TaggingDto getRecipeByItem(@PathVariable Integer itemId) {
        return recipeTaggingService.getRecipeByMenuItemId(itemId);
    }
}
