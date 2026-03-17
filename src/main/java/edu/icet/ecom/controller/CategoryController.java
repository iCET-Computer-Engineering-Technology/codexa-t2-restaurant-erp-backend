package edu.icet.ecom.controller;

import edu.icet.ecom.dto.CategoryDto;
import edu.icet.ecom.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public CategoryDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<CategoryDto> getAll(){
        return service.getAll();
    }
}
