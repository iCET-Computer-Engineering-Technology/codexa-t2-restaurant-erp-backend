package edu.icet.ecom.dto;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabletOrderResponse {
    private Boolean success;
    private String message;
    private OrderResponse order;
    private Integer kdsOrderId;
    private Map<String, Object> details; // For error details
}

