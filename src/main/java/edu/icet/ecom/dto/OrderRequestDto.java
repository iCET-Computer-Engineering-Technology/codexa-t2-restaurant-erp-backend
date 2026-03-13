package edu.icet.ecom.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private Long tableId;
    private Long customerId;
    private List<OrderItemRequestDto> items;
}
