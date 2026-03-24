package edu.icet.ecom.service;

import edu.icet.ecom.entity.OrderAssign;

import java.util.List;

public interface WaiterService {
    boolean serveOrder(Long assignmentId);
    List<OrderAssign> getUnservedOrders(Long waiterId);
}
