package edu.icet.ecom.service;

import java.util.List;
import edu.icet.ecom.entity.OrderAssignment;

public interface WaiterServcie {
    void updateOrderStatus(Integer orderId,Integer waiterId, String status);
    
    List<OrderAssignment> getAssignedOrders(Long waiterId);
}
