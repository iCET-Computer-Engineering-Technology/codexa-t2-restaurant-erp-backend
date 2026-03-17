package edu.icet.ecom.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"modifiers"}) //prevent recursion
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer id;
    private Integer orderId;
    private Integer menuItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal modifierTotal;
    private BigDecimal lineTotal;
    private Integer courseNumber;
    private String status; // NEW ENUM: pending, fired, ready, served, voided
    private String notes;
    private LocalDateTime createdAt;
    private List<OrderItemModifier> modifiers; // not save on DB
}
