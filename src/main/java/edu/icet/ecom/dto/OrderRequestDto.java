package edu.icet.ecom.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long tableId;
    private Long customerId;
    private List<OrderItemRequestDto> items;
}
