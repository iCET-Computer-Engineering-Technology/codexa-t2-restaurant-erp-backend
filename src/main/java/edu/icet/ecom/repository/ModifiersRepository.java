package edu.icet.ecom.repository;

import edu.icet.ecom.dto.ModifiersDto;

import java.util.List;

public interface ModifiersRepository {
    boolean addModifier(ModifiersDto modifierDto);
    boolean updateModifier(ModifiersDto modifierDto);
    boolean deleteModifierById(Integer id);
    List<ModifiersDto> getModifiersByGroupId(Integer groupId);
}
