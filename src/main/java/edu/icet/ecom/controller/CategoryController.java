package edu.icet.ecom.controller;

import edu.icet.ecom.dto.CategoryDto;
import edu.icet.ecom.service.CategoryService;
import lombok.RequiredArgsConstructor;
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
}
