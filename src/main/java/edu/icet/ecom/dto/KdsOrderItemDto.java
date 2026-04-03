package edu.icet.ecom.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KdsOrderItemDto {
    private Integer id;
    private Integer menuItemId;
    private String menuItemName;
    private Integer quantity;
    private String status;
    private LocalDateTime firedAt;
    private LocalDateTime completedAt;
}

