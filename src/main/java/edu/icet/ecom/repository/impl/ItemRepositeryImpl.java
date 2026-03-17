package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.ItemDto;
import edu.icet.ecom.repository.ItemRepositery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositeryImpl implements ItemRepositery {

    private final JdbcTemplate template;

    @Override
    public boolean addItem(ItemDto itemDto) {
        return template.update("INSERT INTO menu_items  (category_id, name, description, base_price, current_price, is_available, is_eightysixed, food_cost_pct, image_url, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?,NOW(),NOW())",
                itemDto.getCategoryId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getBasePrice(),
                itemDto.getCurrentPrice(),
                itemDto.getIsAvailable(),
                itemDto.getIsEightysixed(),
                itemDto.getFoodCostPct(),
                itemDto.getImageUrl()
        )>0;
    }

    @Override
    public boolean updateItem(ItemDto itemDto) {
        return template.update("UPDATE menu_items SET category_id = ?, name = ?, description = ?, base_price = ?, current_price = ?, is_available = ?, is_eightysixed = ?, food_cost_pct = ?, image_url = ?, updated_at = NOW() WHERE id = ?",
                itemDto.getCategoryId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getBasePrice(),
                itemDto.getCurrentPrice(),
                itemDto.getIsAvailable(),
                itemDto.getIsEightysixed(),
                itemDto.getFoodCostPct(),
                itemDto.getImageUrl(),
                itemDto.getId()
        )>0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return template.update("DELETE FROM menu_items WHERE id = ?", id)>0;
    }

    @Override
    public ItemDto searchById(Integer id) {
        return template.queryForObject("SELECT * FROM menu_items WHERE id = ?" , (rs, rowNum) -> new ItemDto(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDouble(5),
                rs.getDouble(6),
                rs.getBoolean(7),
                rs.getBoolean(8),
                rs.getDouble(10),
                rs.getString(11),
                rs.getTimestamp(12),
                rs.getTimestamp(13)
        ) , id);
    }

    @Override
    public List<ItemDto> getAll() {
        return List.of();
    }
}
