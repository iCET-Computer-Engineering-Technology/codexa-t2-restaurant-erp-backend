package edu.icet.ecom.dto;

import lombok.Data;

@Data
public class AssignWaiterRequest {
    private Long orderId;
    private Long waiterId;
}
