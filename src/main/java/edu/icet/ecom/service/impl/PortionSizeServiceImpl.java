package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.PortionSizeDto;
import edu.icet.ecom.repository.PortionSizeRepositery;
import edu.icet.ecom.service.PortionSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortionSizeServiceImpl implements PortionSizeService {

    private final PortionSizeRepositery repositery;

    @Override
    public boolean addSize(PortionSizeDto portionSizeDto) {
        return repositery.addSize(portionSizeDto);
    }

    @Override
    public boolean updateSize(PortionSizeDto portionSizeDto) {
        return repositery.updateSize(portionSizeDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return repositery.deleteById(id);
    }

    @Override
    public PortionSizeDto searchById(Integer id) {
        return repositery.searchById(id);
    }

    @Override
    public List<PortionSizeDto> getAll() {
        return repositery.getAll();
    }
}
