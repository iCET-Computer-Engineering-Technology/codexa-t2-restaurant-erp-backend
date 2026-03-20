package edu.icet.ecom.service;

import edu.icet.ecom.dto.OrderDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto findById(Integer id);
    List<OrderDto> findByStatus(String status);
    List<OrderDto> findAll();
    Boolean updateStatus(Integer orderId, String status);
}
