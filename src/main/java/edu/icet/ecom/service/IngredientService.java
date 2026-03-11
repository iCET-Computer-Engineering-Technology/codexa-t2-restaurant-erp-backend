package edu.icet.ecom.service;

import edu.icet.ecom.dto.IngredientDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IngredientService {
    void add(IngredientDto ingredientDto);
    IngredientDto get(Integer id);
    List<IngredientDto> getAll(Pageable pageable);
    IngredientDto update(Integer id, IngredientDto ingredientDto);
    void delete(Integer id);
}
