package edu.icet.ecom.service;

import edu.icet.ecom.dto.ItemsDto;

import java.util.List;

public interface ItemsService {
    boolean addItem(ItemsDto itemsDto);
    boolean updateItem(ItemsDto itemsDto);
    boolean deleteById(Integer id);
    ItemsDto searchById(Integer id);
    List<ItemsDto> getAll();
}
