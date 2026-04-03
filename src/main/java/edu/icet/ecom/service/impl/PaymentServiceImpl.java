package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.repository.PaymentRepository;
import edu.icet.ecom.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public boolean addPayment(PaymentDto paymentDto) {
        boolean orderExists = paymentRepository
                .checkOrderExists(paymentDto.getOrderId());
        if (!orderExists) {
            throw new IllegalArgumentException(
                    "Order ID " + paymentDto.getOrderId() + " does not exist"
            );
        }

        PaymentDto existing = paymentRepository
                .getPaymentByOrderId(paymentDto.getOrderId());
        if(existing != null){
            throw new RuntimeException(
                    "Payment already exists for Order ID :" +
                            paymentDto.getOrderId()
            );
        }
        paymentDto.setReferenceNumber(generateReferenceNumber(paymentDto.getPaymentMethod()));
        paymentDto.setProcessedAt(
                new Timestamp(System.currentTimeMillis())
        );
        boolean saved = paymentRepository.addPayment(paymentDto);
        if(saved){
            paymentRepository.updateOrderStatus(paymentDto.getOrderId());
        }
        return saved;
    }

    @Override
    public List<PaymentDto> getAllPayment() {
        List<PaymentDto> payments = paymentRepository.getAllPayment();
        if (payments.isEmpty()) {
            throw new RuntimeException("No payments found");
        }
        return payments;
    }

    @Override
    public PaymentDto getPaymentByOrderId(Integer orderId) {
        return paymentRepository.getPaymentByOrderId(orderId);
    }

    private String generateReferenceNumber(String paymentMethod) {
        String methodCode = paymentMethod.equalsIgnoreCase("card") ? "CC" : "CS";
        String unique = UUID.randomUUID().toString()
                .substring(0, 4)
                .toUpperCase();
        return "TXN-" + methodCode + "-" + unique;
    }

}
