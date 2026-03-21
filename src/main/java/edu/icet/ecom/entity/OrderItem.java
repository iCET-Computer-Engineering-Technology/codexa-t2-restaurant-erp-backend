package edu.icet.ecom.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long menuItemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemModifier> modifiers; // not save on DB
}
