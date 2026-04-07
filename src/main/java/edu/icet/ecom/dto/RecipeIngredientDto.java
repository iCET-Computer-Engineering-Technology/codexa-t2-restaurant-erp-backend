package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDto {
    private Integer id;
    private Integer ingredientId;
    private String ingredientName;
    private BigDecimal quantity;
    private String unit;
}

