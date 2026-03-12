package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuItemVariant;
import edu.icet.ecom.service.MenuItemVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menuItemVariant")
@RequiredArgsConstructor
public class MenuItemVariantController {

    private final MenuItemVariantService service;

    @PostMapping("/add")
    public boolean addItemVariant(@RequestBody MenuItemVariant menuItemVariant){
        return service.addItemVariant(menuItemVariant);
    }

    @PutMapping("/update")
    public boolean updateItemVariant(@RequestBody MenuItemVariant menuItemVariant){
        return service.updateItemVariant(menuItemVariant);
    }

    @DeleteMapping("delete-by-id/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }
}
