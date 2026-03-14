package edu.icet.ecom.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDto {
    private Integer id;
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot e empty")
    @Pattern(regexp = "^07[01245678]\\d{7}",message = "Phone number must be exactly 10 digits and start with 0")
    private String phone;

    private String address;
}

