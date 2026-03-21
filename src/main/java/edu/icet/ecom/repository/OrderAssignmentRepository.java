package edu.icet.ecom.repository;

import edu.icet.ecom.entity.OrderAssignment;

import java.util.List;

public interface OrderAssignmentRepository {

    void assignWaiter(Long kitchenOrderId, Long waiterId);

    List<OrderAssignment> getAssignments();

    boolean existsByKitchenOrderId(Long kitchenOrderId);
}
