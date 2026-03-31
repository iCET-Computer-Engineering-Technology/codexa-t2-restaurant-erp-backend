package edu.icet.ecom.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {
    private Integer id;
    private Integer orderId;
    private String paymentMethod;
    private Double amount;
    private Double tipAmount;
    private String referenceNumber;
    private Integer processedBy;
    private Timestamp processedAt;
}
