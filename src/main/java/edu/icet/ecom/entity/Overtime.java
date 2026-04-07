package edu.icet.ecom.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Overtime {

    private Integer id;
    private Integer employeeId;
    private LocalDate date;
    private Double hours;
    private Double ratePerHour;
    private Double totalAmount;
}
