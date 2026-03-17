package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ItemDto;
import edu.icet.ecom.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public boolean addItem(@RequestBody ItemDto itemDto){
        return service.addItem(itemDto);
    }
}
