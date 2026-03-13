package edu.icet.ecom.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemModifierRequestDto {
    private Long modifierId;
    private String modifierName;
    private BigDecimal priceAdjustment;
    private Integer quantity;
}
