package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MenuItemModifierGroupDto;
import edu.icet.ecom.repository.MenuItemModifierGroupRepository;
import edu.icet.ecom.service.MenuItemModifierGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemModifierGroupServiceImpl implements MenuItemModifierGroupService {

    private final MenuItemModifierGroupRepository repositery;

    @Override
    public boolean assign(MenuItemModifierGroupDto dto) {
        return repositery.assign(dto);
    }

    @Override
    public boolean deleteByMenuItemId(Integer menuItemId) {
        return repositery.deleteByMenuItemId(menuItemId);
    }

    @Override
    public List<MenuItemModifierGroupDto> getByMenuItemId(Integer menuItemId) {
        return repositery.getByMenuItemId(menuItemId);
    }
}
