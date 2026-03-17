package edu.icet.ecom.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDto {
    private Integer id;
    private Integer categoryId;
    private String categoryName;
    private String name;
    private String description;
    private Double basePrice;
    private Double currentPrice;
    private Boolean isAvailable;
    private Boolean isEightysixed;
    private Double foodCostPct;
    private String imageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
