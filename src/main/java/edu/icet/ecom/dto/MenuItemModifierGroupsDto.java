package edu.icet.ecom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuItemModifierGroupsDto {
    private Integer id;
    private Integer menuItemId;
    private Integer modifierGroupId;
    private Integer sortOrder;
}
