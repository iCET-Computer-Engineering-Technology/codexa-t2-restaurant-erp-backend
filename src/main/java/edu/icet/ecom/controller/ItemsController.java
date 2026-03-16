package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ItemsDto;
import edu.icet.ecom.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService service;

    @PostMapping
    public boolean addItem(@RequestBody ItemsDto itemsDto){
        return service.addItem(itemsDto);
    }

    @PutMapping
    public boolean updateItem(@RequestBody ItemsDto itemsDto){
        return service.updateItem(itemsDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public ItemsDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<ItemsDto> getAll(){
        return service.getAll();
    }
}
