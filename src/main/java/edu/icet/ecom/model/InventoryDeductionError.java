package edu.icet.ecom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDeductionError {
    private Integer id;
    private Integer orderItemId;
    private Integer orderId;
    private Integer ingredientId;
    private BigDecimal requiredQuantity;
    private BigDecimal availableQuantity;
    private String reason;
    private LocalDateTime createdAt;
}

