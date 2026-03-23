package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.IngredientDto;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.mapper.IngredientMapper;
import edu.icet.ecom.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(IngredientDto ingredientDto) {
        jdbcTemplate.update(
                "INSERT INTO ingredients(name, current_stock, cost_per_unit, unit) values(?,?,?,?)",
                ingredientDto.getName(),
                ingredientDto.getQuantity(),
                ingredientDto.getPrice(),
                ingredientDto.getDescription()
        );
    }

    @Override
    public IngredientDto get(Integer id) {
        List<IngredientDto> list = jdbcTemplate.query(
                "SELECT id, name, current_stock AS quantity, cost_per_unit AS price, unit AS description FROM ingredients WHERE id = ?",
                new IngredientMapper(),
                id
        );
        return list.stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));
    }

    @Override
    public List<IngredientDto> getAll(int page, int size) {
        int offset = Math.max(page, 0) * Math.max(size, 1);
        return jdbcTemplate.query(
                "SELECT id, name, current_stock AS quantity, cost_per_unit AS price, unit AS description FROM ingredients LIMIT ? OFFSET ?",
                new IngredientMapper(),
                Math.max(size, 1),
                offset
        );
    }

    @Override
    public IngredientDto update(Integer id, IngredientDto dto) {
        int count = jdbcTemplate.update(
                "UPDATE ingredients SET name = ?, current_stock = ?, cost_per_unit = ?, unit = ? WHERE id = ?",
                dto.getName(), dto.getQuantity(), dto.getPrice(), dto.getDescription(), id
        );
        if (count == 0) {
            throw new ResourceNotFoundException("Ingredient not found id " + id);
        }
        return dto;
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM ingredients WHERE id = ?", id);
    }
}
