package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ItemDto;
import edu.icet.ecom.dto.ItemsDto;
import edu.icet.ecom.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public boolean addItem(@RequestBody ItemDto itemDto){
        return service.addItem(itemDto);
    }

    @PutMapping
    public boolean updateItem(@RequestBody ItemDto itemDto){
        return service.updateItem(itemDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public ItemDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<ItemDto> getAll(){
        return service.getAll();
    }

}
