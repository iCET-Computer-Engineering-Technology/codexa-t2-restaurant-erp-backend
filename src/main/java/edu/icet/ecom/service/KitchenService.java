package edu.icet.ecom.service;

import edu.icet.ecom.entity.KitchenOrder;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.entity.WaiterDetails;

import java.util.List;

public interface KitchenService {
    List<KitchenOrder> getKitchenOrders();
    List<Waiter> getActiveWaiters();
    void assignWaiter(Long kitchenOrderId, Long waiterId);

    List<WaiterDetails> getAssignmentsWaiter();

    List<Order> getOpenOrders();
    void sendToKitchen(Long orderId);
    void markOrderReady(Long orderId);
}
