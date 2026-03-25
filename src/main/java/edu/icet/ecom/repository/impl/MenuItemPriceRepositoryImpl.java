package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.MenuItemPriceDto;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.repository.MenuItemPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class MenuItemPriceRepositoryImpl implements MenuItemPriceRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_SELECT =
            "SELECT mip.id, mip.item_id, mi.name, mc.name, mip.portion_id, p.portion_name, mip.price, mip.is_active " +
                    "FROM menu_item_price mip " +
                    "JOIN menu_items mi ON mip.item_id = mi.id " +
                    "LEFT JOIN menu_categories mc ON mi.category_id = mc.id " +
                    "JOIN portions p ON mip.portion_id = p.id ";


    private MenuItemPriceDto mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        MenuItemPriceDto dto = new MenuItemPriceDto();

        dto.setId(rs.getInt(1));
        dto.setItemId(rs.getInt(2));
        dto.setItemName(rs.getString(3));
        dto.setCategoryName(rs.getString(4));
        dto.setPortionId(rs.getInt(5));
        dto.setPortionName(rs.getString(6));
        dto.setPrice(rs.getDouble(7));
        dto.setIsActive(rs.getBoolean(8));

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
        return jdbcTemplate.update(
                "UPDATE menu_item_price SET item_id = ?, portion_id = ?, price = ?, is_active = ? WHERE id = ?",
                menuItemPrice.getItemId(),
                menuItemPrice.getPortionId(),
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
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM menu_item_price WHERE id = ?", (rs, rowNum) -> new MenuItemPriceDto(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getInt(3),
                    rs.getDouble(4),
                    rs.getBoolean(5)
            ) , id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Menu item price not found: id=" + id);
        }
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

    @Override
    public List<MenuItemPriceDto> getFullMenu() {
        return jdbcTemplate.query(BASE_SELECT + "ORDER BY mc.name, mi.name, p.id",
                (rs, rowNum) -> mapRow(rs));
    }
}
