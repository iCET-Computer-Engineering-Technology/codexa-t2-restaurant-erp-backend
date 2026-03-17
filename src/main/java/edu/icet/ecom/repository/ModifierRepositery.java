package edu.icet.ecom.repository;

import edu.icet.ecom.dto.ModifierDto;

import java.util.List;

public interface ModifierRepositery {
    boolean addModifier(ModifierDto modifierDto);
    boolean updateModifier(ModifierDto modifierDto);
    boolean deleteModifierById(Integer id);
    List<ModifierDto> getModifiersByGroupId(Integer groupId);
}
