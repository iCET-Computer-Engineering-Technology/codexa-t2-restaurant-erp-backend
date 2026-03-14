package edu.icet.ecom.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemModifierRequestDto {
    private Long modifierId;
    private String modifierName;
    private BigDecimal priceAdjustment;
    private Integer quantity;
}
