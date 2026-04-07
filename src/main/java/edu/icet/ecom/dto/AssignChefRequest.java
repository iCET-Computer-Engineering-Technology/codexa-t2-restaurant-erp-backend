package edu.icet.ecom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignChefRequest {
    @NotNull(message = "kitchenOrderId is required")
    private Long kitchenOrderId;

    @NotNull(message = "chefId is required")
    private Long chefId;
}
