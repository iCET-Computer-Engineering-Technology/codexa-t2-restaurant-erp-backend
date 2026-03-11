package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.IngredientDto;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.mapper.IngredientMapper;
import edu.icet.ecom.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void add(IngredientDto ingredientDto) {
        jdbcTemplate.update("INSERT INTO ingredients(id, name, quantity, price, description) values(?,?,?,?,?)",
                ingredientDto.getId(), ingredientDto.getName(), ingredientDto.getQuantity(), ingredientDto.getPrice(),
                ingredientDto.getDescription());
    }

    @Override
    public IngredientDto get(Integer id){
        List<IngredientDto> list = jdbcTemplate.query("SELECT id,name,quantity,price,description FROM ingredients " +
                "WHERE id = ?", new IngredientMapper(), id);
        return list.stream().findFirst().orElseThrow(()->new ResourceNotFoundException("Ingredient not found"));
    }

    @Override
    public List<IngredientDto> getAll(Pageable pageable) {
        return jdbcTemplate.query("SELECT id, name, quantity, price, description FROM ingredients LIMIT ? OFFSET ?",
                new  IngredientMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public IngredientDto update(Integer id, IngredientDto dto) {
        int count =  jdbcTemplate.update("UPDATE ingredients SET id = ? , name = ? , quantity = ? , price = ? , " +
                "description = ? WHERE id = ?", dto.getId(), dto.getName(), dto.getQuantity(), dto.getPrice(), dto.getDescription(), id);
        if (count == 0) {
            throw new ResourceNotFoundException("Parent not found id "+ id);
        }
        return dto;
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM ingredients WHERE id = ?", id);
    }
}
