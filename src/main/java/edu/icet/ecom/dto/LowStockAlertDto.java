package edu.icet.ecom.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LowStockAlertDto {
    private Integer alertId;
    private Integer ingredientId;
    private String ingredientName;
    private BigDecimal currentStock;
    private BigDecimal threshold;
    private String unit;
    private String reorderLink;
    private LocalDateTime triggeredAt;
}

