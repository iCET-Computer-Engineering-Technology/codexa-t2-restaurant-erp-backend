package edu.icet.ecom.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuItemsDto {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String description;
    private Boolean isActive;
    private String imageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
