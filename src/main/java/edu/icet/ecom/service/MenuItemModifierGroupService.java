package edu.icet.ecom.service;

import edu.icet.ecom.dto.MenuItemModifierGroupDto;

import java.util.List;

public interface MenuItemModifierGroupService {
    boolean assign(MenuItemModifierGroupDto dto);
    boolean deleteByMenuItemId(Integer menuItemId);
    List<MenuItemModifierGroupDto> getByMenuItemId(Integer menuItemId);
}
