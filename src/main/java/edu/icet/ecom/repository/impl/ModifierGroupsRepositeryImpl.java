package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.ModifierGroupDto;
import edu.icet.ecom.repository.ModifierGroupsRepositery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ModifierGroupsRepositeryImpl implements ModifierGroupsRepositery {

    private final JdbcTemplate template;

    @Override
    public boolean addModifierGroup(ModifierGroupDto modifierGroupDto) {
        return template.update("INSERT INTO modifier_groups (name, selection_type, is_required) VALUES (?,?,?)",
                modifierGroupDto.getName(),
                modifierGroupDto.getSelectionType(),
                modifierGroupDto.getIsRequired()
        ) > 0;
    }

    @Override
    public boolean updateModifierGroup(ModifierGroupDto modifierGroupDto) {
        return template.update("UPDATE modifier_groups SET name = ?, selection_type = ?, is_required = ? WHERE id = ?",
                modifierGroupDto.getName(),
                modifierGroupDto.getSelectionType(),
                modifierGroupDto.getIsRequired(),
                modifierGroupDto.getId()
        ) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return template.update("DELETE FROM modifier_groups WHERE id = ?", id) > 0;
    }

    @Override
    public ModifierGroupDto searchById(Integer id) {
        return template.queryForObject("SELECT * FROM modifier_groups WHERE id = ?", (rs, rowNum) -> new ModifierGroupDto(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getBoolean(4)
        ), id);
    }

    @Override
    public List<ModifierGroupDto> getAll() {
        return template.query("SELECT * FROM modifier_groups" , (rs, rowNum) -> new ModifierGroupDto(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getBoolean(4)
        ));
    }

    @Override
    public List<ModifierGroupDto> getByMenuItemId(Integer menuItemId) {
        return template.query(
                "SELECT mg.* FROM modifier_groups mg " +
                        "JOIN menu_item_modifier_groups mimg ON mg.id = mimg.modifier_group_id " +
                        "WHERE mimg.menu_item_id = ?",
                (rs, rowNum) -> new ModifierGroupDto(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBoolean(4)
                ), menuItemId);
    }
}
