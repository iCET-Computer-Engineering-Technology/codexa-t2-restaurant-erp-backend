package edu.icet.ecom.service;

import edu.icet.ecom.dto.MenuCategoriesDto;

import java.util.List;

public interface MenuCategoriesService {
    boolean addCategory(MenuCategoriesDto categoryDto);
    boolean updateCategory(MenuCategoriesDto categoryDto);
    boolean deleteById(Integer id);
    MenuCategoriesDto searchById(Integer id);
    List<MenuCategoriesDto> getAll();
}
