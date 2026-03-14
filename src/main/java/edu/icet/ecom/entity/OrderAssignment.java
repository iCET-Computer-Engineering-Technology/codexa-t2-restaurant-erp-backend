package edu.icet.ecom.entity;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderAssignment {
    private Long id;
    private Long orderId;
    private Long waiterId;
    private LocalDateTime assignedAt;
}
