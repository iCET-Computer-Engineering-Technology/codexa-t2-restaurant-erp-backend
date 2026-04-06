package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Integer id;
    private Integer menuItemId;
    private Integer versionNumber;
    private Boolean isCurrent;
    private String notes;
    private List<RecipeIngredientDto> ingredients;
}

