package edu.icet.ecom.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReconciliationReportDto {
    private LocalDate date;
    private Double posTotal;
    private Double actualRevenue;
    private Double discrepancy;
    private String status;
}
