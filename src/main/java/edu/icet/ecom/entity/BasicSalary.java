package edu.icet.ecom.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BasicSalary {
    private Integer id;
    private String roleName;
    private Double amount;
}
