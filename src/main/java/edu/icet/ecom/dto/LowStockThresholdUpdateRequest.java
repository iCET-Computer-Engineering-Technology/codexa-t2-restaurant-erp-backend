package edu.icet.ecom.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LowStockThresholdUpdateRequest {
    private BigDecimal threshold;
}

