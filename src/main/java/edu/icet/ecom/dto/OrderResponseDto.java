package edu.icet.ecom.dto;

import edu.icet.ecom.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private List<OrderItem> items;
}
