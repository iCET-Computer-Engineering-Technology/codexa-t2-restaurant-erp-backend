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
public class TablePositionDto {
    private Integer id;
    private String tableNumber;
    private Integer capacity;
    private Integer sectionId;
    private Integer posX;
    private Integer posY;
    private String status;
    private LocalDateTime updatedAt;
}
