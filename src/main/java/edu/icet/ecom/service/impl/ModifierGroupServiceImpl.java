package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ModifierGroupDto;
import edu.icet.ecom.repository.ModifierGroupsRepository;
import edu.icet.ecom.service.ModifierGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModifierGroupServiceImpl implements ModifierGroupService {

    private final ModifierGroupsRepository repositery;
    @Override
    public boolean addModifierGroup(ModifierGroupDto modifierGroupDto) {
        return repositery.addModifierGroup(modifierGroupDto);
    }

    @Override
    public boolean updateModifierGroup(ModifierGroupDto modifierGroupDto) {
        return repositery.updateModifierGroup(modifierGroupDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public ModifierGroupDto searchById(Integer id) {
        return repositery.searchById(id);
    }

    @Override
    public List<ModifierGroupDto> getAll() {
        return repositery.getAll();
    }

    @Override
    public List<ModifierGroupDto> getByMenuItemId(Integer menuItemId) {
        return repositery.getByMenuItemId(menuItemId);
    }
}
