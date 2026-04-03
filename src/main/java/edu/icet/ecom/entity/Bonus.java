package edu.icet.ecom.entity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Bonus {
    private Integer id;
    private Integer employeeId;
    private Double amount;
}
