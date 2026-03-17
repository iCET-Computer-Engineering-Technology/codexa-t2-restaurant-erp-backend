package edu.icet.ecom.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModifier {
    private Integer id;
    private Integer orderItemId;
    private Integer modifierId;
    private String modifierName;
    private BigDecimal priceAdjustment;
}
