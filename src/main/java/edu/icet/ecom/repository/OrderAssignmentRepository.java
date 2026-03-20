package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderAssignment;

import java.util.List;

public interface OrderAssignmentRepository {
    void assignWaiter(Long orderId, Long waiterId);
    List<OrderAssignment> getAssignments();
    boolean existsByKitchenOrderId(Long kitchenOrderId);
}
