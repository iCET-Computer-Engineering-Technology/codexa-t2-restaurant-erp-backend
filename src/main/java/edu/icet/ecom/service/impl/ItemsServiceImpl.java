package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ItemsDto;
import edu.icet.ecom.repository.ItemsRepositery;
import edu.icet.ecom.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsServiceImpl implements ItemsService {

    private final ItemsRepositery repositery;

    @Override
    public boolean addItem(ItemsDto itemsDto) {
        return repositery.addItems(itemsDto);
    }

    @Override
    public boolean updateItem(ItemsDto itemsDto) {
        return repositery.updateItem(itemsDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public ItemsDto searchById(Integer id) {
        return repositery.searchById(id);
    }

    @Override
    public List<ItemsDto> getAll() {
        return repositery.getAll();
    }
}
