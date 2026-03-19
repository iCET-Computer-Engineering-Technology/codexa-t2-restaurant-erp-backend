package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.MenuItemPriceDto;
import edu.icet.ecom.dto.MenuItemsDto;
import edu.icet.ecom.repository.MenuItemPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class MenuItemPriceRepositoryImpl implements MenuItemPriceRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_SELECT =
            "SELECT mi.name, mc.name, p.portion_name, mip.price " +
                    "FROM menu_item_price mip " +
                    "JOIN menu_items mi ON mip.item_id = mi.id " +
                    "JOIN menu_categories mc ON mi.category_id = mc.id " +
                    "JOIN portions p ON mip.portion_id = p.id ";

    private MenuItemPriceDto mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        MenuItemPriceDto dto = new MenuItemPriceDto();
        dto.setItemName(rs.getString(1));
        dto.setCategoryName(rs.getString(2));
        dto.setPortionName(rs.getString(3));
        dto.setPrice(rs.getDouble(4));
        return dto;
    }

    @Override
    public boolean addItemPrice(MenuItemPriceDto menuItemPrice) {
        return jdbcTemplate.update("INSERT INTO menu_item_price (item_id, portion_id, price, is_active) VALUES (?,?,?,?)" ,
                menuItemPrice.getItemId(),
                menuItemPrice.getPortionId(),
                menuItemPrice.getPrice(),
                menuItemPrice.getIsActive()
        )>0;
    }

    @Override
    public boolean updateItemPrice(MenuItemPriceDto menuItemPrice) {
        return jdbcTemplate.update("UPDATE menu_item_price SET price = ?, is_active = ? WHERE id = ?",
                menuItemPrice.getPrice(),
                menuItemPrice.getIsActive(),
                menuItemPrice.getId()
        ) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return jdbcTemplate.update(
                "DELETE FROM menu_item_price WHERE id = ?", id
        ) > 0;
    }

    @Override
    public MenuItemPriceDto searchById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM menu_item_price WHERE id = ?", (rs, rowNum) -> new MenuItemPriceDto(
                rs.getInt(1),
                rs.getInt(2),
                rs.getInt(3),
                rs.getDouble(4),
                rs.getBoolean(5)
        ) , id);
    }

    @Override
    public List<MenuItemPriceDto> getAllItemPrice() {
        return jdbcTemplate.query("SELECT * FROM menu_item_price", (rs, rowNum) -> {
            MenuItemPriceDto dto = new MenuItemPriceDto();
            dto.setId(rs.getInt(1));
            dto.setItemId(rs.getInt(2));
            dto.setPortionId(rs.getInt(3));
            dto.setPrice(rs.getDouble(4));
            dto.setIsActive(rs.getBoolean(5));
            return dto;
        });
    }

    @Override
    public List<MenuItemPriceDto> findByItemId(Integer itemId) {
        return jdbcTemplate.query(
                "SELECT mip.id, mip.item_id, mip.portion_id," +
                        "mip.price, mip.is_active " +
                        "FROM menu_item_price mip " +
                        "JOIN portions p ON mip.portion_id = p.id " +
                        "WHERE mip.item_id = ? " +
                        "ORDER BY p.portion_name",
                (rs, row) -> new MenuItemPriceDto (
                        rs.getInt("id"),
                        rs.getInt("item_id"),
                        rs.getInt("portion_id"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_active")
                ), itemId);
    }

    @Override
    public List<MenuItemPriceDto> getPricesByItemId(Integer itemId) {
        return jdbcTemplate.query(BASE_SELECT + "WHERE mip.item_id = ?",
                (rs, rowNum) -> mapRow(rs), itemId);
    }

    // Method 2 — full menu
    @Override
    public List<MenuItemPriceDto> getFullMenu() {
        return jdbcTemplate.query(BASE_SELECT + "ORDER BY mc.name, mi.name, p.id",
                (rs, rowNum) -> mapRow(rs));
    }
}
