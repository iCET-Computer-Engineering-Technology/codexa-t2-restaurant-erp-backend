package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ItemDto;
import edu.icet.ecom.repository.ItemRepositery;
import edu.icet.ecom.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepositery repositery;

    @Override
    public boolean addItem(ItemDto itemDto) {
        return repositery.addItem(itemDto);
    }

    @Override
    public boolean updateItem(ItemDto itemDto) {
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
    }

    @Override
    public ItemDto searchById(Integer id) {
        return null;
    }

    @Override
    public List<ItemDto> getAll() {
        return List.of();
    }
}
