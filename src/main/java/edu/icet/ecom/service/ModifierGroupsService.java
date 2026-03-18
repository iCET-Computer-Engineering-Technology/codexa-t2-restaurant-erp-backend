package edu.icet.ecom.service;

import edu.icet.ecom.dto.ModifierGroupsDto;

import java.util.List;

public interface ModifierGroupsService {
    boolean addModifierGroup(ModifierGroupsDto modifierGroupDto);
    boolean updateModifierGroup(ModifierGroupsDto modifierGroupDto);
    boolean deleteById(Integer id);
    ModifierGroupsDto searchById(Integer id);
    List<ModifierGroupsDto> getAll();

    List<ModifierGroupsDto> getByMenuItemId(Integer menuItemId);
}
