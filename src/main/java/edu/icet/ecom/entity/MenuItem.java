package edu.icet.ecom.entity;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String description;
    private Integer isAvailable;
    private String imageUrl;
    private Integer recipeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
