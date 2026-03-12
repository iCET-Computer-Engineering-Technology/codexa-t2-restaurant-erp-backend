package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.MenuItemVariant;
import edu.icet.ecom.repository.MenuItemVariantRepositery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuItemVariantRepositeryImpl implements MenuItemVariantRepositery {

    private final JdbcTemplate template;

    @Override
    public boolean addItemVariant(MenuItemVariant menuItemVariant) {
        return false;
    }

    @Override
    public boolean updateItemVariant(MenuItemVariant menuItemVariant) {
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
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
