package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.OrderRepository;
import edu.icet.ecom.repository.PaymentRepository;
import edu.icet.ecom.service.PaymentService;
import edu.icet.ecom.service.TableManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TableManagementService tableManagementService;

    @Transactional
    @Override
    public boolean addPayment(PaymentDto paymentDto) {
        if (paymentDto.getOrderId() == null ||
                paymentDto.getPaymentMethod() == null ||
                paymentDto.getPaymentMethod().isBlank() ||
                paymentDto.getAmount() == null) {
            throw new IllegalArgumentException(
                    "Order ID, payment method and amount are required"
            );
        }

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
            
            // Update table status to available after payment completion
            try {
                Order order = orderRepository.findById(paymentDto.getOrderId());
                if (order != null && order.getTableId() != null) {
                    tableManagementService.updateTableStatusAutomatic(
                            order.getTableId(), 
                            "available", 
                            "PAYMENT_COMPLETED"
                    );
                    log.info("Table {} set to available after payment for order {}", 
                            order.getTableId(), paymentDto.getOrderId());
                }
            } catch (Exception e) {
                // Non-blocking: payment succeeds even if table update fails
                log.error("Failed to update table status after payment for order {}: {}", 
                        paymentDto.getOrderId(), e.getMessage());
            }
        }
        return saved;
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.getAllPayments();
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
