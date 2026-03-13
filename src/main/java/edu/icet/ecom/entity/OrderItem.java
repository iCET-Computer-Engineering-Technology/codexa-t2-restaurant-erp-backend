package edu.icet.ecom.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItem {

    private Long id;
    private Long orderId;
    private Long menuItemId;

    private Integer quantity;

    private Double unitPrice;
    private Double totalPrice;

    private LocalDateTime createdAt;

}
