package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemsDto {
    private Integer id;             // PK is now 'id' field
    private Integer categoryId;     // FK to menu_categories
    private String name;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal currentPrice;
    private Boolean isAvailable;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
