package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.PortionsDto;
import edu.icet.ecom.repository.PortionsRepository;
import edu.icet.ecom.service.PortionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortionsServiceImpl implements PortionsService {

    private final PortionsRepository portionsRepository;

    @Override
    public boolean addPortion(PortionsDto portionsDto) {
        return portionsRepository.addPortion(portionsDto);
    }

    @Override
    public boolean updatePortion(PortionsDto portionsDto) {
        return portionsRepository.updatePortion(portionsDto);
    }

    @Override
    public boolean deleteById(Integer id) {
        return portionsRepository.deleteById(id);
    }

    @Override
    public PortionsDto searchById(Integer id) {
        return portionsRepository.searchById(id);
    }

    @Override
    public List<PortionsDto> getAll() {
        return portionsRepository.getAll();
    }

}
