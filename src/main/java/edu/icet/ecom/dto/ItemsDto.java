package edu.icet.ecom.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemsDto {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
