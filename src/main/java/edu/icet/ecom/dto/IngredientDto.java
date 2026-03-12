package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IngredientDto{
    Integer id;
    String name;
    Double quantity;
    Double price;
    String description;
}
