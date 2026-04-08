package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableStatusUpdateEvent {
    private Integer id;
    private String tableNumber;
    private Integer capacity;
    private String status;
    private String eventType;
    private LocalDateTime timestamp;
}
