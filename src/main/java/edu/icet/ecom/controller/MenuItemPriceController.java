package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuItemPriceDto;
import edu.icet.ecom.service.MeuItemPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-item-price")
@RequiredArgsConstructor
public class MenuItemPriceController {

    private final MeuItemPriceService meuItemPriceService;

    @PostMapping
    public boolean addItemPrice(@RequestBody MenuItemPriceDto menuItemPriceDto){
        return meuItemPriceService.addItemPrice(menuItemPriceDto);
    }

    @PutMapping
    public boolean updateItemPrice(@RequestBody MenuItemPriceDto menuItemPriceDto){
        return meuItemPriceService.updateItemPrice(menuItemPriceDto);
    }

    @DeleteMapping("/{id}")
    public  boolean deleteById(@PathVariable Integer id){
        return meuItemPriceService.deleteById(id);
    }

    @GetMapping("/{id}")
    public MenuItemPriceDto searchById(@PathVariable Integer id){
        return meuItemPriceService.searchById(id);
    }

    @GetMapping("/find/{itemId}")
    public List<MenuItemPriceDto> findByItemId(@PathVariable Integer itemId){
        return meuItemPriceService.findByItemId(itemId);
    }

    @GetMapping("/get-price/{itemId}")
    public List<MenuItemPriceDto> getPricesByItemId(@PathVariable Integer itemId){
        return meuItemPriceService.getPricesByItemId(itemId);
    }

        @GetMapping("/get-full-menu")
    public List<MenuItemPriceDto> getFullMenu(){
        return meuItemPriceService.getFullMenu();
    }

    @GetMapping
    public List<MenuItemPriceDto> getAll(){
        return meuItemPriceService.getAll();
    }

}
