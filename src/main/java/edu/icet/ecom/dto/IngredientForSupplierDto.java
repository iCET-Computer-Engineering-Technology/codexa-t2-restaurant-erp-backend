package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientForSupplierDto {
    private Long ingredientId;
    private String ingredientName;
    private String supplierSku;
    private BigDecimal unitPrice;
    private BigDecimal minOrderQty;
    private LocalDate priceDate;
}
