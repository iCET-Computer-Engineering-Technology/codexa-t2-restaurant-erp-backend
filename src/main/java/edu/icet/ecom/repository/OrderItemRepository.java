package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderItemRepository {
    Integer saveAndGetId(OrderItem orderItem); //return Integer
    List<OrderItem> findByOrderId(Integer orderId);
    OrderItem findById(Integer orderItemId);
    boolean updateStatus(Integer orderItemId, String status);
    List<Map<String, Object>> findItemsWithNamesByOrderId(Integer orderId);
    List<Map<String, Object>> findItemsWithNamesByOrderIds(List<Integer> orderIds);
}
