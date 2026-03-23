package edu.icet.ecom.repository;

import edu.icet.ecom.dto.MenuCategoriesDto;

import java.util.List;

public interface MenuCategoriesRepository {
    boolean addCategory(MenuCategoriesDto categoryDto);
    boolean updateCategory(MenuCategoriesDto categoryDto);
    boolean deleteById(Integer id);
    MenuCategoriesDto searchById(Integer id);
    List<MenuCategoriesDto> getAll();
}
