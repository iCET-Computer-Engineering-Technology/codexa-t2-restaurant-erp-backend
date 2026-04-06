package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    private Integer id;
    private String name;
    private String unit;
    private BigDecimal currentStock;
    private BigDecimal lowStockThreshold;
    private BigDecimal costPerUnit;
}

