package edu.icet.ecom.entity;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderAssignment {
    private Long id;
    private Long kitchenOrderId;
    private Long waiterId;
    private LocalDateTime assignedAt;
}
