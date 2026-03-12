package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MenuItemVariant;
import edu.icet.ecom.repository.MenuItemVariantRepositery;
import edu.icet.ecom.service.MenuItemVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemVariantServiceImpl implements MenuItemVariantService {

    private final MenuItemVariantRepositery repositery;

    @Override
    public boolean addItemVariant(MenuItemVariant menuItemVariant) {
        return repositery.addItemVariant(menuItemVariant);
    }

    @Override
    public boolean updateItemVariant(MenuItemVariant menuItemVariant) {
        return repositery.updateItemVariant(menuItemVariant);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public MenuItemVariant searchById(Integer id) {
        return null;
    }

    @Override
    public List<MenuItemVariant> getAll() {
        return List.of();
    }
}
