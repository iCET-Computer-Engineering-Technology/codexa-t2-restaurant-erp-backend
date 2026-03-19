package edu.icet.ecom.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    private Integer menuItemId;
    private Integer quantity;
    private String notes;
}
