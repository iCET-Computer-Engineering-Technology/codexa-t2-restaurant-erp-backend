package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ItemsDto;
import edu.icet.ecom.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService service;

    @PostMapping("/add")
    public boolean addItem(@RequestBody ItemsDto itemsDto){
        return service.addItem(itemsDto);
    }

    @PutMapping("/update")
    public boolean updateItem(@RequestBody ItemsDto itemsDto){
        return service.updateItem(itemsDto);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/search-by-id/{id}")
    public ItemsDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping("/getAll")
    public List<ItemsDto> getAll(){
        return service.getAll();
    }
}
