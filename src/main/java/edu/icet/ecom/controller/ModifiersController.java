package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ModifiersDto;
import edu.icet.ecom.service.ModifiersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modifiers")
@RequiredArgsConstructor
public class ModifiersController {
    private final ModifiersService service;

    @PostMapping
    public boolean addModifier(@RequestBody ModifiersDto modifierDto){
        return service.addModifier(modifierDto);
    }

    @PutMapping
    public boolean updateModifier(@RequestBody ModifiersDto modifierDto){
        return service.updateModifier(modifierDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteModifierById(@PathVariable Integer id) {
        return service.deleteModifierById(id);
    }

    @GetMapping("/{groupId}")
    public List<ModifiersDto> getAllModifiersGroupId(@PathVariable Integer groupId){
        return service.getModifiersByGroupId(groupId);
    }
}
