package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface ReceiptRepository {
    List<Order> findAllPaidOrders();
    Order findPaidOrderById(Integer orderId);
    List<Order> searchPaidOrders(String orderNumber, LocalDate startDate, LocalDate endDate);
}
