package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    private Integer menuItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String notes;
    private List<OrderItemModifierRequestDto> modifiers; // not save on DB
}
