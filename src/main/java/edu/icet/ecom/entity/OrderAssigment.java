package edu.icet.ecom.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderAssigment {
    private Long id;
    private Long waiterId;
    private Long orderId;
    private String status;
    private Long tableId;
    private String orderNumber;
}
