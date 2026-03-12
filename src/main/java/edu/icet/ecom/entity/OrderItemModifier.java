package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
