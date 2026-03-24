package edu.icet.ecom.entity;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private Integer id;
    private Integer user_id;
    private String firstName;
    private String lastName;
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Invalid email format"
    )
    private String email;

    @Pattern(regexp = "^07[01245678]\\d{7}",message = "Phone number must be exactly 10 digits and start with 0")
    private String phone;

    private Double basicSalary;
    private Double allowance;
    private Double bonus;
    private Double donation;

    private String department;
    private String designation;
    private String status; // ACTIVE / INACTIVE

    public Employee(Double allowance,Double bonus,Double donation){
        this.allowance = allowance;
        this.bonus = bonus;
        this.donation = donation;
    }
}
