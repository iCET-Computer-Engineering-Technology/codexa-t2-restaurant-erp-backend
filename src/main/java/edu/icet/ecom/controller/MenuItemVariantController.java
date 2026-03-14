package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuItemVariant;
import edu.icet.ecom.dto.VariantDTO;
import edu.icet.ecom.service.MenuItemVariantService;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/search-by-id/{id}")
    public MenuItemVariant searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping("/getAll")
    public List<VariantDTO> getAll(){
        return service.getAll();
    }
}
