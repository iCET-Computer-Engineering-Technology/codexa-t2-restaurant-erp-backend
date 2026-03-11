package edu.icet.ecom.repository;

public interface OrderAssignmentRepository {

    void assignWaiter(Long orderId, Long waiterId);

}
