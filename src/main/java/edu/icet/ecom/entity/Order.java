package edu.icet.ecom.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"items"}) //to prevent deep recursion
public class Order {
    private Integer id;
    private String orderNumber;
    private String orderType; //dine_in, takeout, delivery, online
    private Integer tableId;
    private Integer customerId;
    private Integer serverId;
    private String status; // open, sent_to_kitchen, partially_ready, ready, paid, voided
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String notes;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> items; // for convenience, not stored in SQL table

}
