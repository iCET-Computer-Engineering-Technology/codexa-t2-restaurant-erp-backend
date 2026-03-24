package edu.icet.ecom.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderAssign {
    private Long id;
    private Long waiterId;
    private Long orderId;
    private String status;
    private Long tableId;
    private String orderNumber;
    private LocalDateTime assignedAt;
}
