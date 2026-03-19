package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Integer    id;            // null on request
    private String     orderNumber;   // null on request
    private String     orderType;     // dine_in | takeout | delivery | online
    private Integer    tableId;       // required for dine_in, null for others
    private Integer    customerId;    // nullable — guest mode
    private Integer    serverId;      // nullable
    private String     status;        // null on request — backend sets "open"
    private BigDecimal subTotal;      // null on request — calculated by backend
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal serviceCharge;
    private BigDecimal totalAmount;
    private String     notes;
    private Timestamp  createdAt;     // null on request, populated on response
    private Timestamp  updatedAt;     // null on request, populated on response
    private List<OrderItemDto> items;
}
