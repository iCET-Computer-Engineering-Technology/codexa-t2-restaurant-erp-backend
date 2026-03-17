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
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
    }

    @Override
    public ItemDto searchById(Integer id) {
        return null;
    }

    @Override
    public List<ItemDto> getAll() {
        return List.of();
    }
}
