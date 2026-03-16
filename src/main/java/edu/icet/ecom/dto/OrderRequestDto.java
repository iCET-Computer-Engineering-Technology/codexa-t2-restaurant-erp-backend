package edu.icet.ecom.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private Long tableId;
    private Long customerId;
    private List<OrderItemRequestDto> items;
}
