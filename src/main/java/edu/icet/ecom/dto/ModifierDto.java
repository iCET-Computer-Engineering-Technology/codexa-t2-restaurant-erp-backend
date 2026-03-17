package edu.icet.ecom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModifierDto {
    private Integer id;
    private Integer groupId;
    private String name;
    private Double priceAdjustment;
    private Boolean isActive;
}
