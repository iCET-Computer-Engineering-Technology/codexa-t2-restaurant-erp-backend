package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.CategoryDto;
import edu.icet.ecom.repository.CategoryRepositery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryRepositeryImpl implements CategoryRepositery {

    private final JdbcTemplate template;

    @Override
    public boolean addCategory(CategoryDto categoryDto) {
        return template.update("INSERT INTO category (name)" + " VALUES (?)",
                categoryDto.getName()
        )>0;
    }

    @Override
    public boolean updateCategory(CategoryDto categoryDto) {
        return template.update("UPDATE category SET name = ? WHERE id = ?",
                categoryDto.getName(),
                categoryDto.getId()
        )>0;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public CategoryDto searchById(Long id) {
        return null;
    }

    @Override
    public List<CategoryDto> getAll() {
        return List.of();
    }
}
