package edu.icet.ecom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VariantDTO {
    private Integer variantId;
    private String itemName;
    private String sizeName;
    private Double price;
    private Boolean isAvailable;
}
