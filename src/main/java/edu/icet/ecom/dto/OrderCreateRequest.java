package edu.icet.ecom.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    private Integer orderTypeId;
    @NotBlank(message = "orderType is required")
    private String orderType;
    private Integer tableId;
    private Integer customerId;
    private Integer serverId;
    private String notes;
    @Valid
    @NotEmpty(message = "items must contain at least 1 item")
    private List<OrderItemCreateRequest> items;
}
