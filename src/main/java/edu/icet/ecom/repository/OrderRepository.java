package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.dto.OrderWithItemNameResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    Integer saveAndGetId(Order order);
    boolean updateStatus(Integer orderId,String status);
    boolean updateType(Integer orderId, String type);
    Order findById(Integer id);
    List<Order> findAll();
    List<Order> findByStatus(String status);
    int upsertAndGetSequence(LocalDate date);
    List<OrderWithItemNameResponse> findAllOrdersWithItemNames();
    OrderWithItemNameResponse findOrderWithItemNamesById(Integer id);
}

