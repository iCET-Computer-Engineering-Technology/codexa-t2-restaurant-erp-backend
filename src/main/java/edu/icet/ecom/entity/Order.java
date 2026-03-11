package edu.icet.ecom.entity;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long tableId;
    private Long customerId;
    private String orderNumber;
    private String status;
    private Double totalAmount;
    private Double tax;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
