package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MenuCategoriesDto;
import edu.icet.ecom.repository.MenuCategoriesRepository;
import edu.icet.ecom.service.MenuCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MenuCategoriesServiceImpl implements MenuCategoriesService {

    private final MenuCategoriesRepository repositery;

    @Override
    public boolean addCategory(MenuCategoriesDto categoryDto) {
        return repositery.addCategory(categoryDto);
    }

    @Override
    public boolean updateCategory(MenuCategoriesDto categoryDto) {
        return repositery.updateCategory(categoryDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public MenuCategoriesDto searchById(Integer id) {
        return repositery.searchById(id);
    }

    @Override
    public List<MenuCategoriesDto> getAll() {
        return repositery.getAll();
    }
}
