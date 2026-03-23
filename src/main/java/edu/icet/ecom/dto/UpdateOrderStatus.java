package edu.icet.ecom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateOrderStatus {
    private Integer orderId;
    private Integer waiterId;
    private String status;
}
