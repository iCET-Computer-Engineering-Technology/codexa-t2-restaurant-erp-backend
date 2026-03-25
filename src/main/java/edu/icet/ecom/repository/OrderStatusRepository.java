package edu.icet.ecom.repository;

public interface OrderStatusRepository {
    void updateOrderStatus(Integer orderId, Integer waiterId, String status);
}
