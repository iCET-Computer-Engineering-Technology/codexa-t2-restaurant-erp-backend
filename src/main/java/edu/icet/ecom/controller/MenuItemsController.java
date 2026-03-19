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

    private final MenuItemsService menuItemsService;

    @PostMapping
    public boolean addItem(@RequestBody MenuItemsDto itemDto){
        return menuItemsService.addItem(itemDto);
    }

    @PutMapping
    public boolean updateItem(@RequestBody MenuItemsDto itemDto){
        return menuItemsService.updateItem(itemDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return menuItemsService.deleteById(id);
    }

    @GetMapping("/{id}")
    public MenuItemsDto searchById(@PathVariable Integer id){
        return menuItemsService.searchById(id);
    }

    @GetMapping
    public List<MenuItemsDto> getAll(){
        return menuItemsService.getAll();
    }

    @GetMapping("/category/{categoryId}")
    public List<MenuItemsDto> getItemByCategoryId(Integer categoryId){
        return menuItemsService.getItemByCategoryId(categoryId);
    }

    @GetMapping("/available-items")
    public List<MenuItemsDto> getAllAvailableItems(){
        return menuItemsService.getAvailableItems();
    }

}
