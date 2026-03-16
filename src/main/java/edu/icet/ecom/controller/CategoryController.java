package edu.icet.ecom.controller;

import edu.icet.ecom.dto.CategoryDto;
import edu.icet.ecom.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public boolean addCategory(@RequestBody CategoryDto categoryDto){
        return service.addCategory(categoryDto);
    }

    @PutMapping
    public boolean updateCategory(@RequestBody CategoryDto categoryDto){
        return service.updateCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Long id){
        return service.deleteById(id);
    }
}
