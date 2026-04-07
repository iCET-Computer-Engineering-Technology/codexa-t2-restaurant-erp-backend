package edu.icet.ecom.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class KitchenOrder {
    private Long id;
    private Long orderId;
    private Long chefId;
    private String status;
    private LocalDateTime getTime;
    private LocalDateTime endTime;
}
