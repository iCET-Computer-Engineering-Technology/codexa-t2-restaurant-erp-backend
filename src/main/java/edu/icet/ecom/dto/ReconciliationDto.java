package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReconciliationDto {
    private LocalDate reconciliationDate;
    private double totalOrderAmount;
    private double totalPaymentAmount;
    private double discrepancy;
    private String status;
}
