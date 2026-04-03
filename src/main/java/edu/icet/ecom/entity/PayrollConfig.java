package edu.icet.ecom.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PayrollConfig {

    private Integer id;
    private Double epfEmployeeRate; // default 0.08
    private Double epfEmployerRate; // default 0.12
    private Double etfRate;         // default 0.03
}
