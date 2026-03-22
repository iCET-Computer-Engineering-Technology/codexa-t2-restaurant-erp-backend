package edu.icet.ecom.repository;

import edu.icet.ecom.entity.WaiterDetails;

import java.util.List;

public interface OrderAssignmentRepository {

    void assignWaiter(Long orderId, Long waiterId);

    List<WaiterDetails> getAssignments();

}
