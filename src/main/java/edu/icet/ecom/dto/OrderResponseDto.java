package edu.icet.ecom.dto;

import edu.icet.ecom.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private List<OrderItem> items;
}
