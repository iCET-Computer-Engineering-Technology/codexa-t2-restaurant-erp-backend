package edu.icet.ecom.repository;

import edu.icet.ecom.dto.ItemDto;

import java.util.List;

public interface ItemRepositery {
    boolean addItem(ItemDto itemDto);
    boolean updateItem(ItemDto itemDto);
    boolean deleteById(Integer id);
    ItemDto searchById(Integer id);
    List<ItemDto> getAll();
}
