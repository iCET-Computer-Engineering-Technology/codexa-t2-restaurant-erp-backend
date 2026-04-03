package edu.icet.ecom.service;

import edu.icet.ecom.dto.IngredientDto;

import java.util.List;

public interface IngredientService {
    void add(IngredientDto ingredientDto);
    IngredientDto get(Integer id);
    List<IngredientDto> getAll(int page, int size);
    IngredientDto update(Integer id, IngredientDto ingredientDto);
    void delete(Integer id);
    void deductInventoryForOrderItems(List<edu.icet.ecom.entity.OrderItem> orderItems);
}
