package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.MenuCategoriesDto;
import edu.icet.ecom.repository.MenuCategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuCategoriesRepositoryImpl implements MenuCategoriesRepository {

    private final JdbcTemplate template;

    @Override
    public boolean addCategory(MenuCategoriesDto categoryDto) {
        return template.update("INSERT INTO menu_categories (name , sort_order , is_active)" + " VALUES (?,?,?)",
                categoryDto.getName(),
                categoryDto.getSortOrder(),
                categoryDto.getIsActive()
        )>0;
    }

    @Override
    public boolean updateCategory(MenuCategoriesDto categoryDto) {
        return template.update("UPDATE menu_categories SET name = ? , sort_order = ? , is_active = ? WHERE id = ?",
                categoryDto.getName(),
                categoryDto.getSortOrder(),
                categoryDto.getIsActive(),
                categoryDto.getId()
        )>0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return template.update("DELETE FROM menu_categories WHERE id = ?" , id)>0;
    }

    @Override
    public MenuCategoriesDto searchById(Integer id) {
        return template.queryForObject("SELECT * FROM menu_categories WHERE id = ?" , (rs, rowNum) -> new MenuCategoriesDto(
                rs.getInt(1),
                rs.getString(2),
                rs.getInt(3),
                rs.getBoolean(4)
                ), id);
    }

    @Override
    public List<MenuCategoriesDto> getAll() {
        return template.query("SELECT * FROM menu_categories", (rs, rowNum) -> new MenuCategoriesDto(
                rs.getInt(1),
                rs.getString(2),
                rs.getInt(3),
                rs.getBoolean(4)
        ) );
    }
}
