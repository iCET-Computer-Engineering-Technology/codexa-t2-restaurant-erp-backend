package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItemDTO {
    private Integer id;
    private String itemName;
    private String portionName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal lineTotal;
    private String notes;
}
