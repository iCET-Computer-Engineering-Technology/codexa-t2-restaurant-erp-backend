package edu.icet.ecom.dto;

import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabletOrderRequest {
    @NotBlank(message = "orderType is required")
    private String orderType; // "dine_in"

    private Integer tableId;
    private Integer customerId;
    private Integer serverId;
    private String notes;

    @NotBlank(message = "idempotencyKey is required for tablet orders")
    private String idempotencyKey; // UUID

    @Valid
    @NotEmpty(message = "items must contain at least 1 item")
    private List<OrderItemCreateRequest> items;
}
