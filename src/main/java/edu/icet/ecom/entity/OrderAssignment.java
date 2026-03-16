package edu.icet.ecom.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderAssignment {
    private Long id;
    private Long orderId;
    private Long waiterId;
    private LocalDateTime assignedAt;

    }

