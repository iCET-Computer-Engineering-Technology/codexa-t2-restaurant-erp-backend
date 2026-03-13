package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderItem;

public interface OrderItemRepository {
    Long saveAndGetId(OrderItem orderItem);
}
