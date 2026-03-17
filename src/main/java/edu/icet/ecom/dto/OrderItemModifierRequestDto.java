package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemModifierRequestDto {
    private Long modifierId;
    private String modifierName;
    private BigDecimal priceAdjustment;
    private Integer quantity;
}
