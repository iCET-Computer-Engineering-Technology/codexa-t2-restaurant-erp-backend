package edu.icet.ecom.repository;

import edu.icet.ecom.dto.MenuItemsDto;

import java.util.List;

public interface MenuItemsRepository {
    boolean addItem(MenuItemsDto itemDto);
    boolean updateItem(MenuItemsDto itemDto);
    boolean deleteById(Integer id);
    MenuItemsDto searchById(Integer id);
    List<MenuItemsDto> getAll();
    List<MenuItemsDto> getItemByCategoryId(Integer categoryId);
}
