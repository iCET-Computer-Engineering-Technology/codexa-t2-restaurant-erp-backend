package edu.icet.ecom.repository;

import edu.icet.ecom.dto.ItemsDto;

import java.util.List;

public interface ItemsRepositery {
    boolean addItems(ItemsDto itemsDto);
    boolean updateItem(ItemsDto itemsDto);
    boolean deleteById(Integer id);
    ItemsDto searchById(Integer id);
    List<ItemsDto> getAll();
}
