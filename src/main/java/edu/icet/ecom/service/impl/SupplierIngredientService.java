package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.IngredientForSupplierDto;
import edu.icet.ecom.dto.SupplierWithIngredientsDto;

import java.util.List;

public interface SupplierIngredientService {
    List<SupplierWithIngredientsDto> getAllSuppliersWithIngredients();
    List<IngredientForSupplierDto> getIngredientsBySupplier(Long supplierId);
}
