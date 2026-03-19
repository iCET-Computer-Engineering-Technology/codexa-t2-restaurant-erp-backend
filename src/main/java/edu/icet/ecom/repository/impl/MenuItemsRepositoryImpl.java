package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.MenuItemsDto;
import edu.icet.ecom.repository.MenuItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuItemsRepositoryImpl implements MenuItemsRepository {

    private final JdbcTemplate template;

    @Override
    public boolean addItem(MenuItemsDto itemDto) {
        return template.update("INSERT INTO menu_items  (category_id, name, description, is_available, image_url, created_at, updated_at) VALUES (?,?,?,?,?,NOW(),NOW())",
                itemDto.getCategoryId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getIsAvailable(),
                itemDto.getImageUrl()
        ) > 0;
    }

    @Override
    public boolean updateItem(MenuItemsDto itemDto) {
        return template.update("UPDATE menu_items SET category_id = ?, name = ?, description = ?, is_available = ?, image_url = ?, updated_at = NOW() WHERE id = ?",
                itemDto.getCategoryId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getIsAvailable(),
                itemDto.getImageUrl(),
                itemDto.getId()
        ) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return template.update("DELETE FROM menu_items WHERE id = ?", id) > 0;
    }

    @Override
    public MenuItemsDto searchById(Integer id) {
        return null;
    }

    @Override
    public List<MenuItemsDto> getAll() {
        return List.of();
    }

}

