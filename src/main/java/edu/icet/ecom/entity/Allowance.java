package edu.icet.ecom.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Allowance {

    private Integer id;
    private Integer employeeId;

    private String type; // Transport / Meal / Other
    private Double amount;
}
