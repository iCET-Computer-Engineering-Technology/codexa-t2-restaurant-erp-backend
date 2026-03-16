package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.CategoryDto;
import edu.icet.ecom.repository.CategoryRepositery;
import edu.icet.ecom.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositery repositery;

    @Override
    public boolean addCategory(CategoryDto categoryDto) {
        return repositery.addCategory(categoryDto);
    }

    @Override
    public boolean updateCategory(CategoryDto categoryDto) {
        return repositery.updateCategory(categoryDto);
    }

    @Override
    public boolean deleteById(Long id) {
        return repositery.deleteById(id);
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
