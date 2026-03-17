package edu.icet.ecom.controller;

import edu.icet.ecom.dto.ModifierDto;
import edu.icet.ecom.service.ModifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modifiers")
@RequiredArgsConstructor
public class ModifierController {
    private final ModifierService service;

    @PostMapping
    public boolean addModifier(@RequestBody ModifierDto modifierDto){
        return service.addModifier(modifierDto);
    }

    @PutMapping
    public boolean updateModifier(@RequestBody ModifierDto modifierDto){
        return service.updateModifier(modifierDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteModifierById(@PathVariable Integer id) {
        return service.deleteModifierById(id);
    }

    @GetMapping("/{groupId}")
    public List<ModifierDto> getAllModifiersGroupId(@PathVariable Integer groupId){
        return service.getModifiersByGroupId(groupId);
    }
}
