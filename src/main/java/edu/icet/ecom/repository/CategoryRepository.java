package edu.icet.ecom.repository;

import edu.icet.ecom.dto.CategoryDto;

import java.util.List;

public interface CategoryRepository {
    boolean addCategory(CategoryDto categoryDto);
    boolean updateCategory(CategoryDto categoryDto);
    boolean deleteById(Integer id);
    CategoryDto searchById(Integer id);
    List<CategoryDto> getAll();
}
