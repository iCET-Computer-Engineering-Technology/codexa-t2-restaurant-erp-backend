package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.CategoryDto;
import edu.icet.ecom.repository.CategoryRepository;
import edu.icet.ecom.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repositery;

    @Override
    public boolean addCategory(CategoryDto categoryDto) {
        return repositery.addCategory(categoryDto);
    }

    @Override
    public boolean updateCategory(CategoryDto categoryDto) {
        return repositery.updateCategory(categoryDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public CategoryDto searchById(Integer id) {
        return repositery.searchById(id);
    }

    @Override
    public List<CategoryDto> getAll() {
        return repositery.getAll();
    }
}
