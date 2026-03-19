package edu.icet.ecom.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuItemPriceDto {
    private Integer id;
    private Integer itemId;
    private Integer portionId;
    private Double price;
    private Boolean isActive;
    private String itemName;
    private String categoryName;
    private String portionName;

    public MenuItemPriceDto(Integer id, Integer itemId, Integer portionId,
                            Double price, Boolean isActive) {
        this.id = id;
        this.itemId = itemId;
        this.portionId = portionId;
        this.price = price;
        this.isActive = isActive;
    }

}
