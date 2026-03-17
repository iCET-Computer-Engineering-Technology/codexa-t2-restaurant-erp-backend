package edu.icet.ecom.dto;

import edu.icet.ecom.entity.OrderItem;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Integer id;
    private String orderNumber;
    private String orderType;
    private String status;
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String source;
    private List<OrderItem> items;
}
