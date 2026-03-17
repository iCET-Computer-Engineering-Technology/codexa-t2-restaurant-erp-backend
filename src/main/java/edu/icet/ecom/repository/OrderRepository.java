package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    Integer saveAndGetId(Order order);
    int upsertAndGetSequence(LocalDate date); //for generate order num
    List<Order> findOpenOrders();
    boolean updateStatus(Integer orderId,String status);
}

