package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ModifierGroupDto;
import edu.icet.ecom.service.ModifierGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modifierGroup")
@RequiredArgsConstructor
public class ModifierGroupController {

    private final ModifierGroupService service;

    @PostMapping
    public boolean addModifierGroup(@RequestBody ModifierGroupDto modifierGroupDto){
        return service.addModifierGroup(modifierGroupDto);
    }

    @PutMapping
    public boolean updateModifierGroup(@RequestBody ModifierGroupDto modifierGroupDto){
        return service.updateModifierGroup(modifierGroupDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public ModifierGroupDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<ModifierGroupDto> getAll(){
        return service.getAll();
    }
}
