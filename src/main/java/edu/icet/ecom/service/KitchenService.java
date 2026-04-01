package edu.icet.ecom.service;

import edu.icet.ecom.entity.*;

import java.util.List;

public interface KitchenService {
    List<KitchenOrder> getKitchenOrders();
    List<Waiter> getActiveWaiters();
    void assignWaiter(Long kitchenOrderId, Long waiterId);
    List<WaiterDetails> getAssignmentsWaiter();
    List<Order> getOpenOrders();
    void sendToKitchen(Long orderId);
    void markOrderReady(Long orderId);
    List<Chef> getAvailableChefs();
    void assignChef(Long orderId, Long chefId);
}
