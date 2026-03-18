package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ModifierGroupsDto;
import edu.icet.ecom.service.ModifierGroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modifier-groups")
@RequiredArgsConstructor
public class ModifierGroupsController {

    private final ModifierGroupsService service;

    @PostMapping
    public boolean addModifierGroup(@RequestBody ModifierGroupsDto modifierGroupDto){
        return service.addModifierGroup(modifierGroupDto);
    }

    @PutMapping
    public boolean updateModifierGroup(@RequestBody ModifierGroupsDto modifierGroupDto){
        return service.updateModifierGroup(modifierGroupDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public ModifierGroupsDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<ModifierGroupsDto> getAll(){
        return service.getAll();
    }

    @GetMapping("/by-item/{menuItemId}")
    public List<ModifierGroupsDto> getByMenuItemId(@PathVariable Integer menuItemId) {
        return service.getByMenuItemId(menuItemId);
    }
}
