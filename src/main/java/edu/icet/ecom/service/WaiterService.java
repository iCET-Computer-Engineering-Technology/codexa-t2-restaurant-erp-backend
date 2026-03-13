package edu.icet.ecom.service;

import edu.icet.ecom.entity.OrderAssigment;

import java.util.List;

public interface WaiterService {
    List<OrderAssigment> getUnservedOrders();
    boolean serveOrder(Long orderId);


}
