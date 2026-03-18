package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuCategoriesDto;
import edu.icet.ecom.service.MenuCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class MenuCategoriesController {

    private final MenuCategoriesService service;

    @PostMapping
    public boolean addCategory(@RequestBody MenuCategoriesDto categoryDto){
        return service.addCategory(categoryDto);
    }

    @PutMapping
    public boolean updateCategory(@RequestBody MenuCategoriesDto categoryDto){
        return service.updateCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public MenuCategoriesDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<MenuCategoriesDto> getAll(){
        return service.getAll();
    }
}
