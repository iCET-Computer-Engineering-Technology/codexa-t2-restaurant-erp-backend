package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.repository.PaymentRepository;
import edu.icet.ecom.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    @Override
    public boolean addPayment(PaymentDto paymentDto) {
        return paymentRepository.addPayment(paymentDto);
    }

    @Override
    public List<PaymentDto> getAllPayment() {
        return paymentRepository.getAllPayment();
    }
}
