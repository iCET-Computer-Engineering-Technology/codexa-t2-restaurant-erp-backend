package edu.icet.ecom.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Deduction {

    private Integer id;
    private Integer employeeId;

    private String type;
    private Double amount;
}
