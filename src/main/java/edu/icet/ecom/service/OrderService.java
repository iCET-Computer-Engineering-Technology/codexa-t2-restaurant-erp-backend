package edu.icet.ecom.service;

import edu.icet.ecom.dto.OrderRequestDto;
import edu.icet.ecom.dto.OrderResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
}
