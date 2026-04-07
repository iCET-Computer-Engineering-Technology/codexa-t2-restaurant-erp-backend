package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableStatusDto {
    private Integer id;
    private String tableNumber;
    private Integer capacity;
    private String status;
}
