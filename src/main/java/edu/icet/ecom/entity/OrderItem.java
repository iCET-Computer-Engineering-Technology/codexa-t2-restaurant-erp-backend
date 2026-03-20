package edu.icet.ecom.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer id;
    private Integer orderId;
    private Integer menuItemId;
    private Integer portionId;
    private Integer quantity;
    private BigDecimal price;
    private String status; // NEW ENUM: pending, fired, ready, served, voided
    private String notes;
    private LocalDateTime createdAt;
}
