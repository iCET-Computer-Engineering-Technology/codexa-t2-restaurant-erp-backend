package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    private Integer orderId;
    private String orderNumber;
    private String orderType;
    private Integer tableId;
    private String status;
    private BigDecimal subTotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal serviceCharge;
    private BigDecimal totalAmount;
    private String notes;
    private LocalDateTime createdAt;
    private List<ReceiptItemDTO> items;
    private String paymentMethod;
    private BigDecimal paymentAmount;
    private BigDecimal tipAmount;
    private String referenceNumber;
    private String processedBy;
    private LocalDateTime processedAt;
}
