package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemDto {
    private Integer    id;
    private Integer    orderId;
    private Integer    menuItemId;
    private Integer    portionId;
    private Integer    quantity;
    private BigDecimal price;         // required on request — frontend sends price
    private BigDecimal lineTotal;     // defined for response
    private String     status;
    private String     notes;
    private Timestamp  createdAt;
}
