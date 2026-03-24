package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MenuItemPriceDto;
import edu.icet.ecom.repository.MenuItemPriceRepository;
import edu.icet.ecom.service.MeuItemPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemPriceServiceImpl implements MeuItemPriceService {

    private final MenuItemPriceRepository menuItemPriceRepository;

    @Override
    public boolean addItemPrice(MenuItemPriceDto menuItemPrice) {
        return menuItemPriceRepository.addItemPrice(menuItemPrice);
    }

    @Override
    public boolean updateItemPrice(MenuItemPriceDto menuItemPrice) {
        return menuItemPriceRepository.updateItemPrice(menuItemPrice);
    }

    @Override
    public boolean deleteById(Integer id) {
        return menuItemPriceRepository.deleteById(id);
    }

    @Override
    public MenuItemPriceDto searchById(Integer id) {
        return menuItemPriceRepository.searchById(id);
    }

    @Override
    public List<MenuItemPriceDto> getAll() {
        return menuItemPriceRepository.getAll();
    }

    @Override
    public List<MenuItemPriceDto> findByItemId(Integer itemId) {
        return menuItemPriceRepository.findByItemId(itemId);
    }

    @Override
    public List<MenuItemPriceDto> getPricesByItemId(Integer itemId) {
        return menuItemPriceRepository.getPricesByItemId(itemId);
    }

    @Override
    public List<MenuItemPriceDto> getFullMenu() {
        return menuItemPriceRepository.getFullMenu();
    }
}
