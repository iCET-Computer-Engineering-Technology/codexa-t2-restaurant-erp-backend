package edu.icet.ecom.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDto {
    private Integer id;
    @NotBlank(message = "Name cannot be empty")
    private String firstName;
    @NotBlank(message = "Name cannot be empty")
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone number cannot e empty")
    @Pattern(regexp = "^07[01245678]\\d{7}",message = "Phone number must be exactly 10 digits and start with 0")
    private String phone;
    private String preferredLanguage;
    private String dietaryNotes;
    private Integer communicationEmail;
    private Integer communicationSms;
    private Integer gdprDeleted;

}

