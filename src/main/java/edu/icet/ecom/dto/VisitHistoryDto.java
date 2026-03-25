package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitHistoryDto {
    private LocalDateTime visitDate;
    private BigDecimal spendAmount;
    private String orderType;
    private String notes;
    private String orderNumber;
}
