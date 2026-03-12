package edu.icet.ecom.controller;

import edu.icet.ecom.dto.IngredientDto;
import edu.icet.ecom.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientService ingredientService;

    @PostMapping
    public void create(@RequestBody IngredientDto dto) {
        ingredientService.add(dto);
    }
    @GetMapping("/{id}")
    public IngredientDto get(@PathVariable Integer id) {
        return ingredientService.get(id);
    }
    @GetMapping
    public List<IngredientDto> getAll(Pageable pageable) {
        return ingredientService.getAll(pageable);
    }
    @PutMapping("/{id}")
    public IngredientDto update(@PathVariable Integer id,@RequestBody IngredientDto dto) {
        return ingredientService.update(id, dto);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ingredientService.delete(id);
    }
}
