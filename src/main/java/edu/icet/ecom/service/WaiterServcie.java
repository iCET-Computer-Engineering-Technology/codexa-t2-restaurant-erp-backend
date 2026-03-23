package edu.icet.ecom.service;

public interface WaiterServcie {
    void updateOrderStatus(Integer orderId,Integer waiterId, String status);
}
