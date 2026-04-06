package edu.icet.ecom.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLeave {

    private Integer id;
    private Integer employeeId;

    private String leaveType; // ANNUAL / CASUAL / SICK
    private LocalDate startDate;
    private LocalDate endDate;

    private Integer totalDays;
    private String status; // PENDING / APPROVED / REJECTED
    private String reason;
}
