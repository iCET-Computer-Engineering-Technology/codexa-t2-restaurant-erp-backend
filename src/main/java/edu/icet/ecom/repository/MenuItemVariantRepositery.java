package edu.icet.ecom.repository;

import edu.icet.ecom.dto.MenuItemVariant;
import edu.icet.ecom.dto.VariantDTO;

import java.util.List;

public interface MenuItemVariantRepositery {
    boolean addItemVariant(MenuItemVariant menuItemVariant);
    boolean updateItemVariant(MenuItemVariant menuItemVariant);
    boolean deleteById(Integer id);
    MenuItemVariant searchById(Integer id);
    List<VariantDTO> getAll();
}
