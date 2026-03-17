package edu.icet.ecom.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignWaiterRequest {
    private Integer orderId;
    private Long waiterId;
}
