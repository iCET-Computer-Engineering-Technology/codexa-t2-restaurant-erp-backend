package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.ItemDto;
import edu.icet.ecom.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

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
        String sql = "SELECT mi.id, mi.category_id, mc.name, mi.name, mi.description, " +
                "mi.base_price, mi.current_price, mi.is_available, mi.is_eightysixed, " +
                "mi.eightysixed_at, mi.food_cost_pct, mi.image_url, mi.created_at, mi.updated_at " +
                "FROM menu_items mi " +
                "JOIN menu_categories mc ON mi.category_id = mc.id " +
                "WHERE mi.id = ?";
        //String sql = "SELECT * FROM menu_items WHERE id = ?";
        return template.queryForObject( sql, (rs, rowNum) -> new ItemDto(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getDouble(6),
                rs.getDouble(7),
                rs.getBoolean(8),
                rs.getBoolean(10),
                rs.getDouble(11),
                rs.getString(12),
                rs.getTimestamp(13),
                rs.getTimestamp(14)
        ) , id);
    }

    @Override
    public List<ItemDto> getAll() {
        String sql = "SELECT mi.id, mi.category_id, mc.name, mi.name, mi.description, " +
                "mi.base_price, mi.current_price, mi.is_available, mi.is_eightysixed, " +
                "mi.eightysixed_at, mi.food_cost_pct, mi.image_url, mi.created_at, mi.updated_at " +
                "FROM menu_items mi " +
                "JOIN menu_categories mc ON mi.category_id = mc.id";
        //String sql = "SELECT * FROM menu_items";
        return template.query(sql , (rs, rowNum) -> new ItemDto(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getDouble(6),
                rs.getDouble(7),
                rs.getBoolean(8),
                rs.getBoolean(10),
                rs.getDouble(11),
                rs.getString(12),
                rs.getTimestamp(13),
                rs.getTimestamp(14)
        ));
    }
}
