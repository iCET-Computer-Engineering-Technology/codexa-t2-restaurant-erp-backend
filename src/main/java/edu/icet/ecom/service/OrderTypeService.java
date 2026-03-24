package edu.icet.ecom.service;

import edu.icet.ecom.dto.OrderTypeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderTypeService {
    List<OrderTypeDto> getAllOrderTypes();
    OrderTypeDto getOrderTypeById(Integer id);
    OrderTypeDto getOrderTypeByName(String typeName);
    List<OrderTypeDto> getActiveOrderTypes();
}

