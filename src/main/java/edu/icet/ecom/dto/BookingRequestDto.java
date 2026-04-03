package edu.icet.ecom.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for creating a reservation with customer details
 * This combines reservation and customer information for the 4-step booking flow
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingRequestDto {

    // Step 1: Select Date & Time
    @NotNull(message = "Reservation date cannot be null")
    @FutureOrPresent(message = "Reservation date must be today or in the future")
    private LocalDate reservationDate;

    @NotNull(message = "Reservation time cannot be null")
    private LocalTime reservationTime;

    // Step 2: Choose Party Size
    @NotNull(message = "Party size cannot be null")
    @Min(value = 1, message = "Party size must be at least 1")
    @Max(value = 20, message = "Party size cannot exceed 20")
    @JsonAlias({"guestCount"})
    private Integer partySize;

    // Step 3: Enter Details
    @NotBlank(message = "Name cannot be empty")
    private String customerName;

    @Email(message = "Invalid email format")
    @JsonAlias({"customerEmail"})
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @JsonAlias({"customerPhone"})
    private String phone;

    // Optional - auto-assigned from available tables if missing
    private Integer tableId;

    @JsonAlias({"specialRequests"})
    private String notes;

    // Step 4: Confirm Booking (confirmation handled by service)
}
