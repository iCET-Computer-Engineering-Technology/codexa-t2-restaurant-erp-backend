package edu.icet.ecom.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KdsOrderItem {
    private Integer id;
    private Integer kdsOrderId;
    private Integer orderItemId;
    private String status;
    private LocalDateTime firedAt;
    private LocalDateTime completedAt;
}

