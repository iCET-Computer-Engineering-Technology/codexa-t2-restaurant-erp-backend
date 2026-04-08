package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    private Integer id;
    private Integer recipeId;
    private Integer ingredientId;
    private BigDecimal quantity;
    private String unit;
}

