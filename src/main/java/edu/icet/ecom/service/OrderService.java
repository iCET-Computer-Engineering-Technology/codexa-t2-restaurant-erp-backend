package edu.icet.ecom.service;

import edu.icet.ecom.dto.OrderCreateRequest;
import edu.icet.ecom.dto.OrderResponse;
import edu.icet.ecom.dto.OrderWithItemNameResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
    OrderResponse findById(Integer id);
    List<OrderResponse> findByStatus(String status);
    List<OrderResponse> findAll();
    Boolean updateStatus(Integer orderId, String status);
    Boolean updateType(Integer orderId, String type);
    List<OrderWithItemNameResponse> getAllOrdersWithItemNames();
    OrderWithItemNameResponse getOrderWithItemNamesById(Integer id);
}
