package edu.icet.ecom.controller;

import edu.icet.ecom.dto.MenuItemModifierGroupDto;
import edu.icet.ecom.service.MenuItemModifierGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-item-modifier-group")
@RequiredArgsConstructor
public class MenuItemModifierGroupController {

    private final MenuItemModifierGroupService service;

    @PostMapping
    public boolean assign(@RequestBody MenuItemModifierGroupDto dto) {
        return service.assign(dto);
    }

    @DeleteMapping("/{menuItemId}")
    public boolean deleteByMenuItemId(@PathVariable Integer menuItemId) {
        return service.deleteByMenuItemId(menuItemId);
    }

    @GetMapping("/{menuItemId}")
    public List<MenuItemModifierGroupDto> getByMenuItemId(@PathVariable Integer menuItemId) {
        return service.getByMenuItemId(menuItemId);
    }
}
