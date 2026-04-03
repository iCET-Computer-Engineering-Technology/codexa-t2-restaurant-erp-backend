package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    Integer saveAndGetId(OrderItem orderItem); //return Integer
    List<OrderItem> findByOrderId(Integer orderId);
    OrderItem findById(Integer orderItemId);
    boolean updateStatus(Integer orderItemId, String status);
}
