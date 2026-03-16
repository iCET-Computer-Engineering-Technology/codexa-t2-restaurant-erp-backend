package edu.icet.ecom.service;

import edu.icet.ecom.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    boolean addCategory(CategoryDto categoryDto);
    boolean updateCategory(CategoryDto categoryDto);
    boolean deleteById(Long id);
    CategoryDto searchById(Long id);
    List<CategoryDto> getAll();
}
