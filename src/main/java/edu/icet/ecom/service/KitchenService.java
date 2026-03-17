package edu.icet.ecom.service;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.OrderAssignment;
import edu.icet.ecom.entity.Waiter;

import java.util.List;

public interface KitchenService {

    List<Order> getKitchenOrders();

    List<Waiter> getActiveWaiters();

    void assignWaiter(Integer orderId,Long waiterId);

    List<OrderAssignment> getAssignments();

}
