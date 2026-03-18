package edu.icet.ecom.service;

import edu.icet.ecom.dto.MenuItemsDto;

import java.util.List;

public interface MenuItemsService {
    boolean addItem(MenuItemsDto itemDto);
    boolean updateItem(MenuItemsDto itemDto);
    boolean deleteById(Integer id);
    MenuItemsDto searchById(Integer id);
    List<MenuItemsDto> getAll();
}
