package edu.icet.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WaiterOrderDto {
    private Integer id;
    private String orderNumber;
    private String orderType;
    private Integer tableId;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
