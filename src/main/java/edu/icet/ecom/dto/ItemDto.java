package edu.icet.ecom.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean isActive;
    private Integer portionId;
    private Integer categoryId;
    private  Double price;
    private Timestamp createdAt;
}
