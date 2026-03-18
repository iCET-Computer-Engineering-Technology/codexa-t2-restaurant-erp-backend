package edu.icet.ecom.repository;

import edu.icet.ecom.dto.MenuItemModifierGroupsDto;

import java.util.List;

public interface MenuItemModifierGroupsRepository {
    boolean assign(MenuItemModifierGroupsDto dto);
    boolean deleteByMenuItemId(Integer menuItemId);
    List<MenuItemModifierGroupsDto> getByMenuItemId(Integer menuItemId);
}
