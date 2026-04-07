package edu.icet.ecom.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class StockDiscrepancyDto {
    private Integer sessionId;
    private LocalDate sessionDate;
    private Integer ingredientId;
    private String ingredientName;
    private BigDecimal systemQuantity;
    private BigDecimal countedQuantity;
    private BigDecimal variance;
}

