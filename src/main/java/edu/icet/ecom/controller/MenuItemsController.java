package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuItemsDto;
import edu.icet.ecom.service.MenuItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
@RequiredArgsConstructor
public class MenuItemsController {

    private final MenuItemsService service;

    @PostMapping
    public boolean addItem(@RequestBody MenuItemsDto itemDto){
        return service.addItem(itemDto);
    }

    @PutMapping
    public boolean updateItem(@RequestBody MenuItemsDto itemDto){
        return service.updateItem(itemDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public MenuItemsDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<MenuItemsDto> getAll(){
        return service.getAll();
    }

}
