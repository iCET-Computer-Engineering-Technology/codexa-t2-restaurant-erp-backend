package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    private Long menuItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private List<OrderItemModifierRequestDto> modifiers; // not save on DB
}
