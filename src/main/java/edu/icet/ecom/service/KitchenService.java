package edu.icet.ecom.service;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.entity.WaiterDetails;

import java.util.List;

public interface KitchenService {

    List<Order> getKitchenOrders();

    List<Waiter> getActiveWaiters();

    void assignWaiter(Long orderId,Long waiterId);

    List<WaiterDetails> getAssignments();

}
