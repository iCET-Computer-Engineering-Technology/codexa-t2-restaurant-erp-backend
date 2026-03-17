package edu.icet.ecom.service;

import edu.icet.ecom.dto.ModifierGroupDto;

import java.util.List;

public interface ModifierGroupService {
    boolean addModifierGroup(ModifierGroupDto modifierGroupDto);
    boolean updateModifierGroup(ModifierGroupDto modifierGroupDto);
    boolean deleteById(Integer id);
    ModifierGroupDto searchById(Integer id);
    List<ModifierGroupDto> getAll();

    List<ModifierGroupDto> getByMenuItemId(Integer menuItemId);
}
