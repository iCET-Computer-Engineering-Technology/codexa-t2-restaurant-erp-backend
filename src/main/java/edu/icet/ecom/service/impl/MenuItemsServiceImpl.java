package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MenuItemsDto;
import edu.icet.ecom.repository.MenuItemsRepository;
import edu.icet.ecom.service.MenuItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemsServiceImpl implements MenuItemsService {

    private final MenuItemsRepository menuItemsRepository;

    @Override
    public boolean addItem(MenuItemsDto itemDto) {
        return menuItemsRepository.addItem(itemDto);
    }

    @Override
    public boolean updateItem(MenuItemsDto itemDto) {
        return  menuItemsRepository.updateItem(itemDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return menuItemsRepository.deleteById(id);
    }

    @Override
    public MenuItemsDto searchById(Integer id) {
        return menuItemsRepository.searchById(id);
    }

    @Override
    public List<MenuItemsDto> getAll() {
        return menuItemsRepository.getAll();
    }

    @Override
    public List<MenuItemsDto> getItemByCategoryId(Integer categoryId) {
        return menuItemsRepository.getItemByCategoryId(categoryId);
    }

    @Override
    public List<MenuItemsDto> getAvailableItems() {
        return menuItemsRepository.getAvailableItems();
    }


}
