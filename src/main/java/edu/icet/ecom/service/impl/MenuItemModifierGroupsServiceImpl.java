package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MenuItemModifierGroupsDto;
import edu.icet.ecom.repository.MenuItemModifierGroupsRepository;
import edu.icet.ecom.service.MenuItemModifierGroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemModifierGroupsServiceImpl implements MenuItemModifierGroupsService {

    private final MenuItemModifierGroupsRepository repositery;

    @Override
    public boolean assign(MenuItemModifierGroupsDto dto) {
        return repositery.assign(dto);
    }

    @Override
    public boolean deleteByMenuItemId(Integer menuItemId) {
        return repositery.deleteByMenuItemId(menuItemId);
    }

    @Override
    public List<MenuItemModifierGroupsDto> getByMenuItemId(Integer menuItemId) {
        return repositery.getByMenuItemId(menuItemId);
    }
}
