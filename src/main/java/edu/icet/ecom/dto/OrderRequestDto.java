package edu.icet.ecom.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Integer tableId;
    private Integer customerId;
    private Integer serverId;
    private String orderType;
    private String notes;
    private String source;
    private List<OrderItemRequestDto> items;
}
