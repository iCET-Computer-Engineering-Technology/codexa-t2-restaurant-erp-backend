package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderAssigment;

import java.util.List;

public interface WaiterRepository {
    void assignWaiter(Long orderId, Long waiterId);

    List<OrderAssigment> getAssignments();

    List<OrderAssigment> getUnservedOrders();
}
