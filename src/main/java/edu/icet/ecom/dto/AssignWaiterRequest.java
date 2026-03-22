package edu.icet.ecom.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignWaiterRequest {
    @NotNull(message = "kitchenOrderId is required")
    @Positive(message = "kitchenOrderId must be greater than 0")
    private Long kitchenOrderId;

    @NotNull(message = "waiterId is required")
    @Positive(message = "waiterId must be greater than 0")
    private Long waiterId;
}
