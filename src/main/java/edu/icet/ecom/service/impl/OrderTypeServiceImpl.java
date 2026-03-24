package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.OrderTypeDto;
import edu.icet.ecom.repository.OrderTypeRepository;
import edu.icet.ecom.service.OrderTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTypeServiceImpl implements OrderTypeService {

    private final OrderTypeRepository orderTypeRepository;

    @Override
    public List<OrderTypeDto> getAllOrderTypes() {
        return orderTypeRepository.findAll();
    }

    @Override
    public OrderTypeDto getOrderTypeById(Integer id) {
        return orderTypeRepository.findById(id);
    }

    @Override
    public OrderTypeDto getOrderTypeByName(String typeName) {
        return orderTypeRepository.findByTypeName(typeName);
    }

    @Override
    public List<OrderTypeDto> getActiveOrderTypes() {
        return orderTypeRepository.findAllActive();
    }
}

