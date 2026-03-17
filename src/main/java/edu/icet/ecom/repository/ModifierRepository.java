package edu.icet.ecom.repository;

import edu.icet.ecom.dto.ModifierDto;

import java.util.List;

public interface ModifierRepository {
    boolean addModifier(ModifierDto modifierDto);
    boolean updateModifier(ModifierDto modifierDto);
    boolean deleteModifierById(Integer id);
    List<ModifierDto> getModifiersByGroupId(Integer groupId);
}
