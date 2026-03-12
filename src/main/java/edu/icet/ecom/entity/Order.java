package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long tableId;
    private Long customerId;
    private String orderNumber;
    private String status; // RECEIVED, PREPARING, READY, COMPLETED, CANCELLED
    private BigDecimal totalAmount;
    private BigDecimal tax;
    private String paymentStatus; // PENDING, PAID, REFUNDED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> items; // for convenience, not stored in SQL table
}
