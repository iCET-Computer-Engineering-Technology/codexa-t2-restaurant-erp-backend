package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderAssign;
import edu.icet.ecom.entity.Waiter;

import java.util.List;

public interface WaiterRepository {
    void assignWaiter(Long orderId, Long waiterId);

    List<OrderAssign> getUnservedOrders(Long waiterId);

    List<OrderAssign> getAssignments();

    List<Waiter> findActiveWaiters();

    boolean markOrderServed(Long assignmentId);
}