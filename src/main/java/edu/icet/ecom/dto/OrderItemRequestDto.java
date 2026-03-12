package edu.icet.ecom.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemRequestDto {
    private Long menuItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private List<OrderItemModifierRequestDto> modifiers; // not save on DB
}
