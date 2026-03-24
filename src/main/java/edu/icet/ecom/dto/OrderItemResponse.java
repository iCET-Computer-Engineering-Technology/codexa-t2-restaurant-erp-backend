package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Integer id;
    private Integer orderId;
    private Integer menuItemId;
    private Integer portionId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal lineTotal;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
}

