package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Ingredient;

import java.math.BigDecimal;
import java.util.List;

public interface IngredientInventoryRepository {
    List<Ingredient> findByIdsForUpdate(List<Integer> ingredientIds);
    boolean deductStock(Integer ingredientId, BigDecimal quantity);
    BigDecimal findCurrentStock(Integer ingredientId);
    Ingredient findById(Integer ingredientId);
    boolean updateLowStockThreshold(Integer ingredientId, BigDecimal threshold);
}
