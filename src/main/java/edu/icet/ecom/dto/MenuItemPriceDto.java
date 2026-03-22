package edu.icet.ecom.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuItemPriceDto {
    private Integer id;

    @NotNull(message = "itemId is required")
    @Positive(message = "itemId must be greater than 0")
    private Integer itemId;

    @NotNull(message = "portionId is required")
    @Positive(message = "portionId must be greater than 0")
    private Integer portionId;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    private Double price;

    @NotNull(message = "isActive is required")
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
