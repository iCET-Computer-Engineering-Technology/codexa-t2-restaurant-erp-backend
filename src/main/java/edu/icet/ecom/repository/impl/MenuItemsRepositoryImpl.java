package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.MenuItemsDto;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.repository.MenuItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuItemsRepositoryImpl implements MenuItemsRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_SELECT =
            "SELECT mi.id, mi.category_id, mc.name, " +
                    "mi.name, mi.description, mi.is_available, mi.image_url " +
                    "FROM menu_items mi " +
                    "LEFT JOIN menu_categories mc ON mi.category_id = mc.id ";

    private MenuItemsDto mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        MenuItemsDto dto = new MenuItemsDto();

        dto.setId(rs.getInt(1));
        dto.setCategoryId(rs.getInt(2));
        dto.setCategoryName(rs.getString(3));
        dto.setName(rs.getString(4));
        dto.setDescription(rs.getString(5));
        dto.setIsAvailable(rs.getBoolean(6));
        dto.setImageUrl(rs.getString(7));

        return dto;
    }

    @Override
    public boolean addItem(MenuItemsDto itemDto) {
        return jdbcTemplate.update("INSERT INTO menu_items  (category_id, name, description, prep_time_minutes, is_available, image_url, created_at, updated_at) VALUES (?,?,?,?,?,?,NOW(),NOW())",
                itemDto.getCategoryId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getPrepTime() != null ? itemDto.getPrepTime() : 10,
                itemDto.getIsAvailable(),
                itemDto.getImageUrl()
        ) > 0;
    }

    @Override
    public boolean updateItem(MenuItemsDto itemDto) {
        return jdbcTemplate.update("UPDATE menu_items SET category_id = ?, name = ?, description = ?, prep_time_minutes = ?, is_available = ?, image_url = ?, updated_at = NOW() WHERE id = ?",
                itemDto.getCategoryId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getPrepTime() != null ? itemDto.getPrepTime() : 10,
                itemDto.getIsAvailable(),
                itemDto.getImageUrl(),
                itemDto.getId()
        ) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(Integer id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM menu_items WHERE id = ?",
                Integer.class,
                id
        );

        if (count == null || count == 0) {
            return false;
        }

        try {
            return jdbcTemplate.update("DELETE FROM menu_items WHERE id = ?", id) > 0;
        } catch (DataIntegrityViolationException ex) {
            // Keep historical FK data intact: mark item unavailable instead of hard delete.
            jdbcTemplate.update("UPDATE menu_item_price SET is_active = 0 WHERE item_id = ?", id);
            jdbcTemplate.update("UPDATE menu_items SET is_available = 0, updated_at = NOW() WHERE id = ?", id);
            return true;
        }
    }

    @Override
    public MenuItemsDto searchById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(BASE_SELECT + "WHERE mi.id = ?",
                    (rs, rowNum) -> mapRow(rs), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Menu item not found: id=" + id);
        }
    }

    @Override
    public List<MenuItemsDto> getAll() {
        return jdbcTemplate.query(BASE_SELECT, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public List<MenuItemsDto> getItemByCategoryId(Integer categoryId) {
        return jdbcTemplate.query(BASE_SELECT + "WHERE mi.category_id = ?",
                (rs, rowNum) -> mapRow(rs), categoryId);
    }

    @Override
    public List<MenuItemsDto> getAvailableItems() {
        return jdbcTemplate.query(
                BASE_SELECT + "WHERE mi.is_available = true",
                (rs, rowNum) -> mapRow(rs)
        );
    }
}