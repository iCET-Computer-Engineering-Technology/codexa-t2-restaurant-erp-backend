package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    List<OrderItem> findByOrderId(Integer orderId);
    Integer saveAndGetId(OrderItem orderItem); //return Integer
}
