package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ModifierGroupsDto;
import edu.icet.ecom.repository.ModifierGroupsRepository;
import edu.icet.ecom.service.ModifierGroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModifierGroupsServiceImpl implements ModifierGroupsService {

    private final ModifierGroupsRepository repositery;
    @Override
    public boolean addModifierGroup(ModifierGroupsDto modifierGroupDto) {
        return repositery.addModifierGroup(modifierGroupDto);
    }

    @Override
    public boolean updateModifierGroup(ModifierGroupsDto modifierGroupDto) {
        return repositery.updateModifierGroup(modifierGroupDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public ModifierGroupsDto searchById(Integer id) {
        return repositery.searchById(id);
    }

    @Override
    public List<ModifierGroupsDto> getAll() {
        return repositery.getAll();
    }

    @Override
    public List<ModifierGroupsDto> getByMenuItemId(Integer menuItemId) {
        return repositery.getByMenuItemId(menuItemId);
    }
}
