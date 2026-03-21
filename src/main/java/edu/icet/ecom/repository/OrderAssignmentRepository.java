package edu.icet.ecom.repository;

import edu.icet.ecom.entity.WaiterNameDisplay;

import java.util.List;

public interface OrderAssignmentRepository {

    void assignWaiter(Long orderId, Long waiterId);
    boolean existsByKitchenOrderId(Long kitchenOrderId);
    List<WaiterNameDisplay> getAssignments();

}
