package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    Integer saveAndGetId(Order order);
    boolean updateStatus(Integer orderId,String status);
    Order findById(Integer id);
    List<Order> findAll();
    List<Order> findByStatus();
    List<Order> findOpenOrders();

    int upsertAndGetSequence(LocalDate date); //for generate order num
}

