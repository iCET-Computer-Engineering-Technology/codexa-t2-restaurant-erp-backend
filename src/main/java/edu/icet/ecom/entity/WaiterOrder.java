package edu.icet.ecom.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WaiterOrder {
    private Integer id;
    private String orderNumber;
    private String orderType;
    private Integer tableId;
    private Integer customerId;
    private Integer serverId;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
