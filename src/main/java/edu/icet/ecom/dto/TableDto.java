package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {
    private Integer id;
    private String tableNumber;
    private Integer capacity;
    private Integer sectionId;
    private String status;
}

