package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderAssigment;
import edu.icet.ecom.entity.Waiter;

import java.util.List;

public interface WaiterRepository {
    void assignWaiter(Long orderId, Long waiterId);

    List<OrderAssigment> getAssignments();

    List<OrderAssigment> getUnservedOrders();

    List<Waiter> findActiveWaiters();

}
