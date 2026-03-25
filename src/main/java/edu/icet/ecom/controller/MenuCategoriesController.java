package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuCategoriesDto;
import edu.icet.ecom.service.MenuCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin
public class MenuCategoriesController {

    private final MenuCategoriesService menuCategoriesService;

    @PostMapping
    public boolean addCategory(@RequestBody MenuCategoriesDto categoryDto){
        return menuCategoriesService.addCategory(categoryDto);
    }

    @PutMapping
    public boolean updateCategory(@RequestBody MenuCategoriesDto categoryDto){
        return menuCategoriesService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return menuCategoriesService.deleteById(id);
    }

    @GetMapping("/{id}")
    public MenuCategoriesDto searchById(@PathVariable Integer id){
        return menuCategoriesService.searchById(id);
    }

    @GetMapping("/get-all")
    public List<MenuCategoriesDto> getAll(){
        return menuCategoriesService.getAll();
    }
}
