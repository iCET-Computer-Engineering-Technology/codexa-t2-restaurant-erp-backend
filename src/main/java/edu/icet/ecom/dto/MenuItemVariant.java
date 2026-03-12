package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuItemVariant {
    private Integer variantId;
    private Integer menuItemId;
    private Integer portionSizeId;
    private Double price;
    private Integer prepTimeMinutes;
    private Integer isAvailable;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
