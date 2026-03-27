package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderWithItemNameResponse {
    private Integer id;
    private Integer orderTypeId;
    private String orderNumber;
    private String orderType;
    private Integer tableId;
    private Integer customerId;
    private Integer serverId;
    private String status;
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal serviceCharge;
    private BigDecimal totalAmount;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemsWithNameResponse> items;
}

