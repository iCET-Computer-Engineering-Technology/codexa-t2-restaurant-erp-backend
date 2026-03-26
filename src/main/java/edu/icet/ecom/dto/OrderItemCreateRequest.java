package edu.icet.ecom.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreateRequest {
    @NotNull(message = "menuItemId is required")
    private Integer menuItemId;

    @NotNull(message = "portionId is required")
    private Integer portionId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be >= 1")
    private Integer quantity;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.01", message = "price must be > 0")
    private BigDecimal price;

    private String notes;
}

