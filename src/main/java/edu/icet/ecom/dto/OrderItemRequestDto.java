package edu.icet.ecom.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderItemRequestDto {
    private Long menuItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private List<OrderItemModifierRequestDto> modifiers; // not save on DB
}
