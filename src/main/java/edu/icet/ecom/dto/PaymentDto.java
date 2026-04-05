package edu.icet.ecom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Integer id;
    @NotNull(message = "Order ID is required")
    @Positive(message = "Order ID must be a positive number")
    private Integer orderId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @PositiveOrZero(message = "Tip amount cannot be negative")
    private Double tipAmount;

    private String referenceNumber;

    private Integer processedBy;

    private Timestamp processedAt;
}
