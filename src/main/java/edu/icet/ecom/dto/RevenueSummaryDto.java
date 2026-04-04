package edu.icet.ecom.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RevenueSummaryDto {
    private String channel;
    private Double totalrevenue;
    private LocalDate date;
}
