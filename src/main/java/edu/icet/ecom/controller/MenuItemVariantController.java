package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuItemVariant;
import edu.icet.ecom.dto.VariantDTO;
import edu.icet.ecom.service.MenuItemVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menuItemVariant")
@RequiredArgsConstructor
public class MenuItemVariantController {

    private final MenuItemVariantService service;

    @PostMapping
    public boolean addItemVariant(@RequestBody MenuItemVariant menuItemVariant){
        return service.addItemVariant(menuItemVariant);
    }

    @PutMapping
    public boolean updateItemVariant(@RequestBody MenuItemVariant menuItemVariant){
        return service.updateItemVariant(menuItemVariant);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public MenuItemVariant searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<VariantDTO> getAll(){
        return service.getAll();
    }
}
