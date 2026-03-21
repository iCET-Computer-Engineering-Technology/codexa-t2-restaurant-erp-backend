package edu.icet.ecom.repository;

import edu.icet.ecom.entity.KitchenOrder;

import java.util.List;

public interface KitchenOrderRepository {
    void createKitchenOrder(Long orderId);
    List<KitchenOrder> getKitchenOrders();
    void markAsDone(Long orderId);
    boolean existsByOrderId(Long orderId);
    KitchenOrder findByOrderId(Long orderId);
    KitchenOrder findById(Long id);
}
