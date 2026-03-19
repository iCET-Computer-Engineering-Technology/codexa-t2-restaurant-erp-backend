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
    private String     menuItemName;  // null on request
    private Integer    portionId;
    private String     portionName;   // null on request
    private Integer    quantity;
    private BigDecimal price;         // required on request — frontend sends price
    private BigDecimal lineTotal;     // null on request — computed by backend
    private String     status;
    private String     notes;
    private Timestamp  createdAt;
}
