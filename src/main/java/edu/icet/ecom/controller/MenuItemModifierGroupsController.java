package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuItemModifierGroupsDto;
import edu.icet.ecom.service.MenuItemModifierGroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-item-modifier-groups")
@RequiredArgsConstructor
public class MenuItemModifierGroupsController {

    private final MenuItemModifierGroupsService service;

    @PostMapping
    public boolean assign(@RequestBody MenuItemModifierGroupsDto dto) {
        return service.assign(dto);
    }

    @DeleteMapping("/{menuItemId}")
    public boolean deleteByMenuItemId(@PathVariable Integer menuItemId) {
        return service.deleteByMenuItemId(menuItemId);
    }

    @GetMapping("/{menuItemId}")
    public List<MenuItemModifierGroupsDto> getByMenuItemId(@PathVariable Integer menuItemId) {
        return service.getByMenuItemId(menuItemId);
    }
}
