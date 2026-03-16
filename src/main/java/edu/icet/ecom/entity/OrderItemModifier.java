package edu.icet.ecom.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModifier {
    private Long id;
    private Long orderItemId;
    private Long modifierId;
    private String modifierName;
    private BigDecimal priceAdjustment;
    private Integer quantity; // how many times applied
    private LocalDateTime createdAt;
}
