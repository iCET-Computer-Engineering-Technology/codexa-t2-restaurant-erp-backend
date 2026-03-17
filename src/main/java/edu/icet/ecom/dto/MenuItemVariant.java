package edu.icet.ecom.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuItemVariant {
    private Integer variantId;
    private Integer menuItemId;
    private Integer portionSizeId;
    private Double price;
    private Integer prepTimeMinutes;
    private Boolean isAvailable;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
