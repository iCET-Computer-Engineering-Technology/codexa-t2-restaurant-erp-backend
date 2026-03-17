package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.CategoryDto;
import edu.icet.ecom.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate template;

    @Override
    public boolean addCategory(CategoryDto categoryDto) {
        return template.update("INSERT INTO menu_categories (name , sort_order , is_active)" + " VALUES (?,?,?)",
                categoryDto.getName(),
                categoryDto.getSortOrder(),
                categoryDto.getIsActive()
        )>0;
    }

    @Override
    public boolean updateCategory(CategoryDto categoryDto) {
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
    public CategoryDto searchById(Integer id) {
        return template.queryForObject("SELECT * FROM menu_categories WHERE id = ?" , (rs, rowNum) -> new CategoryDto(
                rs.getInt(1),
                rs.getString(2),
                rs.getInt(3),
                rs.getBoolean(4)
                ), id);
    }

    @Override
    public List<CategoryDto> getAll() {
        return template.query("SELECT * FROM menu_categories", (rs, rowNum) -> new CategoryDto(
                rs.getInt(1),
                rs.getString(2),
                rs.getInt(3),
                rs.getBoolean(4)
        ) );
    }
}
