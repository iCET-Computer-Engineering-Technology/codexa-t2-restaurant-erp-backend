package edu.icet.ecom.service;

import edu.icet.ecom.dto.MenuItemPriceDto;

import java.util.List;

public interface MeuItemPriceService {
    boolean addItemPrice(MenuItemPriceDto menuItemPrice);
    boolean updateItemPrice(MenuItemPriceDto menuItemPrice);
    boolean deleteById(Integer id);
    MenuItemPriceDto searchById(Integer id);
    List<MenuItemPriceDto> getAll();
    List<MenuItemPriceDto> findByItemId(Integer itemId);
    public List<MenuItemPriceDto> getPricesByItemId(Integer itemId);
    public List<MenuItemPriceDto> getFullMenu();
}
