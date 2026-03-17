package edu.icet.ecom.service;

import edu.icet.ecom.dto.ModifierDto;

import java.util.List;

public interface ModifierService {
    boolean addModifier(ModifierDto modifierDto);
    boolean updateModifier(ModifierDto modifierDto);
    boolean deleteModifierById(Integer id);
    List<ModifierDto> getModifiersByGroupId(Integer groupId);
}
