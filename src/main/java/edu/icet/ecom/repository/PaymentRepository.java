package edu.icet.ecom.repository;

import java.util.List;
import java.util.Map;

public interface PaymentRepository {
    Map<String, Object> findPaymentWithUserByOrderId(Integer orderId);
    List<Map<String, Object>> findPaymentsByOrderIds(List<Integer> orderIds);
}
