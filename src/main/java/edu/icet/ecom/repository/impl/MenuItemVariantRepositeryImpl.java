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
        String sql = "INSERT INTO menu_item_variants (menu_item_id , portion_size_id , price , prep_time_minutes , is_available , created_at , updated_at)"+"VALUES(?,?,?,?,?,?,?)";
        return template.update(sql ,
                menuItemVariant.getMenuItemId(),
                menuItemVariant.getPortionSizeId(),
                menuItemVariant.getPrice(),
                menuItemVariant.getPrepTimeMinutes(),
                menuItemVariant.getIsAvailable(),
                menuItemVariant.getCreatedAt(),
                menuItemVariant.getUpdatedAt()
        )>0;
    }

    @Override
    public boolean updateItemVariant(MenuItemVariant menuItemVariant) {
        return template.update("UPDATE menu_item_variants SET menu_item_id = ?, portion_size_id = ? , price = ? , prep_time_minutes = ? , is_available = ? , created_at = ? , updated_at = ? WHERE variant_id = ?" ,
                menuItemVariant.getMenuItemId(),
                menuItemVariant.getPortionSizeId(),
                menuItemVariant.getPrice(),
                menuItemVariant.getPrepTimeMinutes(),
                menuItemVariant.getIsAvailable(),
                menuItemVariant.getCreatedAt(),
                menuItemVariant.getUpdatedAt(),
                menuItemVariant.getVariantId()
        )>0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return template.update("DELETE FROM menu_item_variants WHERE variant_id = ?", id)>1;
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
