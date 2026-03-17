package edu.icet.ecom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModifierGroupDto {
    private Integer id;
    private String name;
    private String selectionType;
    private Boolean isRequired;
}
