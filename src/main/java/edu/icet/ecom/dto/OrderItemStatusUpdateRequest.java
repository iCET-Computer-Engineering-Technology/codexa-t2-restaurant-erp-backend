package edu.icet.ecom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemStatusUpdateRequest {
    @NotBlank(message = "status is required")
    private String status;
}
