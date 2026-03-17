package edu.icet.ecom.repository;

import edu.icet.ecom.dto.MenuItemModifierGroupDto;

import java.util.List;

public interface MenuItemModifierGroupRepositery {
    boolean assign(MenuItemModifierGroupDto dto);
    boolean deleteByMenuItemId(Integer menuItemId);
    List<MenuItemModifierGroupDto> getByMenuItemId(Integer menuItemId);
}
