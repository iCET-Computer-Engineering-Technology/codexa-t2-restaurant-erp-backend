package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.MenuItemModifierGroupsDto;
import edu.icet.ecom.repository.MenuItemModifierGroupsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuItemModifierGroupsRepositoryImpl implements MenuItemModifierGroupsRepository {

    private final JdbcTemplate template;

    @Override
    public boolean assign(MenuItemModifierGroupsDto dto) {
        return template.update("INSERT INTO menu_item_modifier_groups (menu_item_id, modifier_group_id, sort_order) VALUES (?,?,?)",
                dto.getMenuItemId(),
                dto.getModifierGroupId(),
                dto.getSortOrder()
        ) > 0;
    }

    @Override
    public boolean deleteByMenuItemId(Integer menuItemId) {
        return template.update("DELETE FROM menu_item_modifier_groups WHERE menu_item_id = ?", menuItemId) > 0;
    }

    @Override
    public List<MenuItemModifierGroupsDto> getByMenuItemId(Integer menuItemId) {
        return template.query("SELECT * FROM menu_item_modifier_groups WHERE menu_item_id = ?", (rs, rowNum) -> new MenuItemModifierGroupsDto(
                rs.getInt(1),
                rs.getInt(2),
                rs.getInt(3),
                rs.getInt(4)
        ), menuItemId);
    }
}
