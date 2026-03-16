package edu.icet.ecom.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignWaiterRequest {
    private Long orderId;
    private Long waiterId;
}
