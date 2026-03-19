package edu.icet.ecom.repository;

import edu.icet.ecom.dto.MenuItemPriceDto;
import edu.icet.ecom.dto.MenuItemsDto;

import java.util.List;

public interface MenuItemPriceRepository {
    boolean addItemPrice(MenuItemPriceDto menuItemPrice);
    boolean updateItemPrice(MenuItemPriceDto menuItemPrice);
    boolean deleteById(Integer id);
    MenuItemPriceDto searchById(Integer id);
    List<MenuItemPriceDto> getAll();
    List<MenuItemPriceDto> findByItemId(Integer itemId);
    public List<MenuItemPriceDto> getPricesByItemId(Integer itemId);
    public List<MenuItemPriceDto> getFullMenu();

}
