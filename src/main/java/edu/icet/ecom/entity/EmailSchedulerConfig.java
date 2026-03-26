package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailSchedulerConfig {
    private Integer id;
    private LocalTime sendTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

