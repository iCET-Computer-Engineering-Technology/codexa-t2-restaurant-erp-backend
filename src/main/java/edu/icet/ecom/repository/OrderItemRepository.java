package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository {

    List<OrderItem> findByOrderId(Long orderId);

    Long saveAndGetId(OrderItem orderItem);
}
