package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    Long saveAndGetId(Order order);
    int upsertAndGetSequence(LocalDate date); //for generate order num
    List<Order> findReceivedOrders();
    boolean updateStatus(Long orderId,String status);
}

