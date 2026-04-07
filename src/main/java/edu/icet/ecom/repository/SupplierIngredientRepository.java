package edu.icet.ecom.repository;

import edu.icet.ecom.dto.IngredientForSupplierDto;
import edu.icet.ecom.dto.SupplierWithIngredientsDto;

import java.util.List;

public interface SupplierIngredientRepository {
    List<SupplierWithIngredientsDto> findAllSuppliersWithIngredients();
    List<IngredientForSupplierDto> findIngredientsBySupplierId(Long supplierId);
}
