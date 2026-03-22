package edu.icet.ecom.repository;

import edu.icet.ecom.entity.WaiterDetails;

import java.util.List;

public interface OrderAssignmentRepository {

    void assignWaiter(Long kitchenOrderId, Long waiterId);

    boolean existsByKitchenOrderId(Long kitchenOrderId);

    List<WaiterDetails> getAssignments();

    boolean existsByKitchenOrderId(Long kitchenOrderId);
}
