package edu.icet.ecom.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuCategoriesDto {
    private Integer id;
    private String name;
    private Integer sortOrder;
    private Boolean isActive;
}
