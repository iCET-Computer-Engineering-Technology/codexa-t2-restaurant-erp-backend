package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierWithIngredientsDto {
    private Long supplierId;
    private String supplierName;
    private List<IngredientForSupplierDto> ingredients;
}
