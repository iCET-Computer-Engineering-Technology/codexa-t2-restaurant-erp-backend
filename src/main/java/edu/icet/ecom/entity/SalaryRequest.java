package edu.icet.ecom.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SalaryRequest {
    private Integer id;
    private Integer employeeId;
    private Double basicSalary;
    private Double donation;
}
