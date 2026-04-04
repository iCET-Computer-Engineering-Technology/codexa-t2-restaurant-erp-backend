package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemsWithNameResponse {
    private Integer id;
    private Integer orderId;
    private Integer menuItemId;
    private String itemName;
    private Integer portionId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal lineTotal;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
}

