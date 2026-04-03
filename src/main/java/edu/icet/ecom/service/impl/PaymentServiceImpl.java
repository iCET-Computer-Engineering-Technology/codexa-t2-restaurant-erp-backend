package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.repository.PaymentRepository;
import edu.icet.ecom.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    @Override
    public boolean addPayment(PaymentDto paymentDto) {
        paymentDto.setReferenceNumber(generateReferenceNumber());
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
        return paymentRepository.getAllPayment();
    }

    @Override
    public PaymentDto getPaymentByOrderId(Integer orderId) {
        return null;
    }

    private String generateReferenceNumber() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String uniqueRef = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        return "REF-" + date + "-" + uniqueRef;
    }

}
