package edu.icet.ecom.service;

import edu.icet.ecom.dto.ModifiersDto;

import java.util.List;

public interface ModifiersService {
    boolean addModifier(ModifiersDto modifierDto);
    boolean updateModifier(ModifiersDto modifierDto);
    boolean deleteModifierById(Integer id);
    List<ModifiersDto> getModifiersByGroupId(Integer groupId);
}
