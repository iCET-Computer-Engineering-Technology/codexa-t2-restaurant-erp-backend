package edu.icet.ecom.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationDto {
    private Integer id;

    @NotNull(message = "Customer ID cannot be null")
    private Integer customerId;

    private String customerName;
    private String email;
    private String phone;

    @NotNull(message = "Table ID cannot be null")
    private Integer tableId;

    private String tableNumber;

    @NotNull(message = "Party size cannot be null")
    @Min(value = 1, message = "Party size must be at least 1")
    @Max(value = 20, message = "Party size cannot exceed 20")
    private Integer partySize;

    @NotNull(message = "Reservation date cannot be null")
    @FutureOrPresent(message = "Reservation date must be today or in the future")
    private LocalDate reservationDate;

    @NotNull(message = "Reservation time cannot be null")
    private LocalTime reservationTime;

    private String status;
    private String confirmationCode;
    private String notes;
}
