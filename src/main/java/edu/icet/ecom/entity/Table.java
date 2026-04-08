package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    private Integer id;
    private Integer sectionId;
    private String tableNumber;
    private Integer capacity;
    private Integer posX;
    private Integer posY;
    private String status;
    private LocalDateTime updatedAt;
}
