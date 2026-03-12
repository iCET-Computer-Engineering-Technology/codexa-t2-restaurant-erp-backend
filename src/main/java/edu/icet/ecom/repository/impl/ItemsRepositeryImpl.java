package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.ItemsDto;
import edu.icet.ecom.repository.ItemsRepositery;
import edu.icet.ecom.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class ItemsRepositeryImpl implements ItemsRepositery {

    private final JdbcTemplate template;

    public ItemsRepositeryImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public boolean addItems(ItemsDto itemsDto) {
        return template.update("INSERT INTO menu_items (item_name,description,category,is_active,created_at,updated_at)" +
                        "VALUES (?,?,?,?,?,?)",
                itemsDto.getName(),
                itemsDto.getDescription(),
                itemsDto.getCategory(),
                itemsDto.getIsActive(),
                itemsDto.getCreatedAt(),
                itemsDto.getUpdatedAt()
        )>0;
    }

    @Override
    public boolean updateItem(ItemsDto itemsDto) {
        return template.update("UPDATE menu_items SET item_name = ? , description = ? , category = ? , created_at = ? , updated_at = ? WHERE menu_item_id = ?" ,
                itemsDto.getName(),
                itemsDto.getDescription(),
                itemsDto.getCategory(),
                itemsDto.getIsActive(),
                itemsDto.getCreatedAt(),
                itemsDto.getUpdatedAt(),
                itemsDto.getId()
        )>0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return template.update("DELETE FROM menu_items WHERE menu_item_id = ?" , id)>1;
    }

    @Override
    public ItemsDto searchById(Integer id) {
        return template.queryForObject("SELECT * FROM menu_items WHERE menu_item_id = ?",(rs, rowNum) -> new ItemsDto(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getBoolean(5),
                rs.getTimestamp(6),
                rs.getTimestamp(7)
        ), id);
    }

    @Override
    public List<ItemsDto> getAll() {
        String sql = "SELECT * FROM menu_items";
        List<ItemsDto> itemsDtoList = template.query(sql, (rs, rowNum) -> {
            ItemsDto itemsDto = new ItemsDto();
            itemsDto.setId(rs.getInt(1));
            itemsDto.setName(rs.getString(2));
            itemsDto.setDescription(rs.getString(3));
            itemsDto.setCategory(rs.getString(4));
            itemsDto.setIsActive(rs.getBoolean(5));
            itemsDto.setCreatedAt(rs.getTimestamp(6));
            itemsDto.setUpdatedAt(rs.getTimestamp(7));
            return  itemsDto;
        });
        return itemsDtoList;
    }
}
