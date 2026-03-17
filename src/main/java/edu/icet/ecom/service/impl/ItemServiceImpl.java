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
        return  repositery.updateItem(itemDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public ItemDto searchById(Integer id) {
        return repositery.searchById(id);
    }

    @Override
    public List<ItemDto> getAll() {
        return repositery.getAll();
    }
}
