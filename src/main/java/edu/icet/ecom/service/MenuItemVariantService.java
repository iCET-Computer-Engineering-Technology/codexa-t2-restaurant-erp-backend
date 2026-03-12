package edu.icet.ecom.service;

import edu.icet.ecom.dto.MenuItemVariant;

import java.util.List;

public interface MenuItemVariantService {
    boolean addItemVariant(MenuItemVariant menuItemVariant);
    boolean updateItemVariant(MenuItemVariant menuItemVariant);
    boolean deleteById(Integer id);
    MenuItemVariant searchById(Integer id);
    List<MenuItemVariant> getAll();
}
