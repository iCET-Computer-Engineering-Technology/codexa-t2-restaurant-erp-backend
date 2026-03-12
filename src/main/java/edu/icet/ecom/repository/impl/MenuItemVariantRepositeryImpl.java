package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.MenuItemVariant;
import edu.icet.ecom.dto.VariantDTO;
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
        return template.queryForObject("SELECT * FROM menu_item_variants WHERE variant_id = ? " , (rs, rowNum) -> new MenuItemVariant(
                rs.getInt(1),
                rs.getInt(2),
                rs.getInt(3),
                rs.getDouble(4),
                rs.getInt(5),
                rs.getBoolean(6),
                rs.getTimestamp(7),
                rs.getTimestamp(8)
        ), id);
    }

    @Override
    public List<VariantDTO> getAll() {
        String sql = "SELECT v.variant_id , i.item_name , p.size_name , v.price , v.is_available " +
                "FROM menu_item_variants v " +
                "JOIN menu_items i ON v.menu_item_id = i.menu_item_id   " +
                "JOIN portion_sizes p ON v.portion_size_id = p.portion_size_id ";

        return template.query(sql, (rs, rowNum) ->
                new VariantDTO(
                        rs.getInt("variant_id"),
                        rs.getString("item_name"),
                        rs.getString("size_name"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_available")
                )
        );
    }

}
