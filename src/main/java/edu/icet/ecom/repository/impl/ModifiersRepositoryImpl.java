package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.ModifiersDto;
import edu.icet.ecom.repository.ModifiersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ModifiersRepositoryImpl implements ModifiersRepository {

    private final JdbcTemplate template;

    @Override
    public boolean addModifier(ModifiersDto modifierDto) {
        return template.update("INSERT INTO modifiers (group_id, name, price_adjustment, is_active) VALUES (?,?,?,?)",
                modifierDto.getGroupId(),
                modifierDto.getName(),
                modifierDto.getPriceAdjustment(),
                modifierDto.getIsActive()
        ) > 0;
    }

    @Override
    public boolean updateModifier(ModifiersDto modifierDto) {
        return template.update("UPDATE modifiers SET group_id = ?, name = ?, price_adjustment = ?, is_active = ? WHERE id = ?",
                modifierDto.getGroupId(),
                modifierDto.getName(),
                modifierDto.getPriceAdjustment(),
                modifierDto.getIsActive(),
                modifierDto.getId()
        ) > 0;
    }

    @Override
    public boolean deleteModifierById(Integer id) {
        return template.update("DELETE FROM modifiers WHERE id = ?", id) > 0;
    }

    @Override
    public List<ModifiersDto> getModifiersByGroupId(Integer groupId) {
        return template.query("SELECT * FROM modifiers WHERE group_id = ?" , (rs, rowNum) -> new ModifiersDto(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getDouble(4),
                rs.getBoolean(5)
        ) , groupId);
    }
}
