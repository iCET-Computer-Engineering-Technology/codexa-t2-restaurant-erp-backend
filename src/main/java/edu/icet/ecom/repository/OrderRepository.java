package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Order;

import java.util.List;

public interface OrderRepository {

    List<Order> findReceivedOrders();

    void updateStatus(Long orderId,String status);
}
