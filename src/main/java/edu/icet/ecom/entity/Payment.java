package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Integer id;
    private Integer orderId;
    private String paymentMethod;
    private BigDecimal amount;
    private BigDecimal tipAmount;
    private String referenceNumber;
    private Integer processedBy;
    private LocalDateTime processedAt;
}
