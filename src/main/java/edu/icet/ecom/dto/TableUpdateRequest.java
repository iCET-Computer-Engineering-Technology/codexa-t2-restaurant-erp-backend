package edu.icet.ecom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableUpdateRequest {
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "available|occupied|reserved|cleaning", message = "Status must be one of: available, occupied, reserved, cleaning")
    private String status;
}
