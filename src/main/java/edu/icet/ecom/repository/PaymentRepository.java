package edu.icet.ecom.repository;

import edu.icet.ecom.dto.PaymentDto;

import java.util.List;

public interface PaymentRepository {
    boolean addPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayment();
}
