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

    private final MenuItemsRepository repositery;

    @Override
    public boolean addItem(MenuItemsDto itemDto) {
        return repositery.addItem(itemDto);
    }

    @Override
    public boolean updateItem(MenuItemsDto itemDto) {
        return  repositery.updateItem(itemDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public MenuItemsDto searchById(Integer id) {
        return repositery.searchById(id);
    }

    @Override
    public List<MenuItemsDto> getAll() {
        return repositery.getAll();
    }

    @Override
    public List<MenuItemsDto> getItemByCategoryId(Integer categoryId) {
        return repositery.getItemByCategoryId(categoryId);
    }
}
