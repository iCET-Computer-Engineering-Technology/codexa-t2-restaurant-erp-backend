package edu.icet.ecom.service;

import edu.icet.ecom.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    boolean addPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayment();
}
