package edu.icet.ecom.service;

import edu.icet.ecom.dto.MenuItemModifierGroupsDto;

import java.util.List;

public interface MenuItemModifierGroupsService {
    boolean assign(MenuItemModifierGroupsDto dto);
    boolean deleteByMenuItemId(Integer menuItemId);
    List<MenuItemModifierGroupsDto> getByMenuItemId(Integer menuItemId);
}
