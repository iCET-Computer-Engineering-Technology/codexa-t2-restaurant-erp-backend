package edu.icet.ecom.repository;

import edu.icet.ecom.dto.PaymentDto;

import java.util.List;
import java.util.Map;

public interface PaymentRepository {
    boolean addPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayments();
    PaymentDto getPaymentByOrderId(Integer orderId);
    boolean updateOrderStatus(Integer orderId);
    boolean checkOrderExists(Integer orderId);
    Map<String, Object> findPaymentWithUserByOrderId(Integer orderId);
    List<Map<String, Object>> findPaymentsByOrderIds(List<Integer> orderIds);
}
