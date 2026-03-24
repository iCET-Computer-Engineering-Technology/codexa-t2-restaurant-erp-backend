package edu.icet.ecom.repository;

import edu.icet.ecom.dto.OrderTypeDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTypeRepository {
    List<OrderTypeDto> findAll();
    OrderTypeDto findById(Integer id);
    OrderTypeDto findByTypeName(String typeName);
    List<OrderTypeDto> findAllActive();
}

