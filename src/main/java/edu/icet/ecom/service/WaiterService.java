package edu.icet.ecom.service;

import edu.icet.ecom.entity.OrderAssigment;

import java.util.List;

public interface WaiterService {

    boolean serveOrder(Long assignmentId);
    List<OrderAssigment> getUnservedOrders(Long waiterId);
}
