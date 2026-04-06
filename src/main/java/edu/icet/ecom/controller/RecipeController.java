package edu.icet.ecom.controller;

import edu.icet.ecom.dto.RecipeDto;
import edu.icet.ecom.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@CrossOrigin
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody RecipeDto recipeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.createRecipe(recipeDto));
    }

    @GetMapping("/menu-item/{menuItemId}")
    public ResponseEntity<RecipeDto> getRecipeByMenuItemId(@PathVariable Integer menuItemId) {
        return ResponseEntity.ok(recipeService.getRecipeByMenuItemId(menuItemId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Integer id, @RequestBody RecipeDto recipeDto) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, recipeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Integer id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{recipeId}/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredientFromRecipe(@PathVariable Integer recipeId, @PathVariable Integer ingredientId) {
        recipeService.deleteIngredientFromRecipe(recipeId, ingredientId);
        return ResponseEntity.ok().build();
    }
}

