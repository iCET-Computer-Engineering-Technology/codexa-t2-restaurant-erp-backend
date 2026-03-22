package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderAssignment;
import edu.icet.ecom.entity.Waiter;

import java.util.List;

public interface WaiterRepository {
    void assignWaiter(Long orderId, Long waiterId);

    List<OrderAssignment> getUnservedOrders(Long waiterId);

    List<Waiter> findActiveWaiters();
}