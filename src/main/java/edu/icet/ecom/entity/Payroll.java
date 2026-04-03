package edu.icet.ecom.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Payroll {

    private Integer id;
    private Integer employeeId;
    private Double basicSalary;
    private Double epfEmployee;
    private Double epfEmployer;
    private Double etfEmployer;
    private Double allowance;
    private Double bonus;
    private Double donation;
    private Double overtimeAmount;
    private Integer leaveDays;
    private Double leaveDeduction;
    private Double totalDeduction;
    private Double netSalary;
    private Double employerCost;
    private LocalDate payrollDate;
}
