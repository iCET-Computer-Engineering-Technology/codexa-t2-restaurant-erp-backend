package edu.icet.ecom.controller;

import edu.icet.ecom.dto.AvailableSlotDto;
import edu.icet.ecom.dto.BookingRequestDto;
import edu.icet.ecom.dto.ReservationDto;
import edu.icet.ecom.dto.TableDto;
import edu.icet.ecom.entity.Reservation;
import edu.icet.ecom.repository.TableRepository;
import edu.icet.ecom.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"/reservations", "/api/reservations"})
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reservations", description = "Table reservation management APIs - 4-step booking flow with real-time availability")
public class ReservationController {

    private final ReservationService reservationService;
    private final TableRepository tableRepository;

    /**
     * Step 1: Get available time slots for a specific date and party size
     * Real-time availability check
     */
    @GetMapping("/available-slots")
    @Operation(summary = "Get available time slots", description = "Returns available time slots for a given date and party size")
    public ResponseEntity<Map<String, Object>> getAvailableSlots(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer partySize) {
        log.info("GET request for available slots - date: {}, party size: {}", date, partySize);

        // Validate date parameter
        if (date == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Required parameter 'date' is missing. Format: YYYY-MM-DD (e.g., 2026-03-27)");
            errorResponse.put("errorCode", "MISSING_DATE");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Validate partySize parameter
        if (partySize == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Required parameter 'partySize' is missing. Must be a number between 1 and 20");
            errorResponse.put("errorCode", "MISSING_PARTY_SIZE");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (date.isBefore(LocalDate.now())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Reservation date cannot be in the past. Provided date: " + date + ", Current date: " + LocalDate.now());
            errorResponse.put("errorCode", "DATE_IN_PAST");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (partySize < 1 || partySize > 20) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Party size must be between 1 and 20 people. Provided: " + partySize);
            errorResponse.put("errorCode", "INVALID_PARTY_SIZE");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            List<AvailableSlotDto> slots = reservationService.getAvailableSlots(date, partySize);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", slots);
            response.put("message", "Available slots retrieved successfully");
            response.put("count", slots.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching available slots: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to fetch available slots: " + e.getMessage());
            errorResponse.put("errorCode", "INTERNAL_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Step 2-4: Create a new reservation
     * Handles the complete 4-step booking flow with instant email confirmation
     */
    @PostMapping("/book")
    @Operation(summary = "Book a reservation", description = "Creates a new reservation with customer details and sends confirmation email")
    public ResponseEntity<Map<String, Object>> bookReservation(@Valid @RequestBody BookingRequestDto bookingRequest) {
        log.info("POST request to book reservation for customer: {}", bookingRequest.getCustomerName());

        try {
            Reservation reservation = reservationService.createReservation(bookingRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reservation confirmed successfully");
            response.put("bookingReference", reservation.getConfirmationCode());
            response.put("confirmationCode", reservation.getConfirmationCode());
            response.put("reservationId", reservation.getId());
            response.put("reservationDate", reservation.getReservationDate());
            response.put("reservationTime", reservation.getReservationTime());
            response.put("partySize", reservation.getPartySize());
            response.put("customerName", reservation.getCustomerName());
            response.put("customerEmail", reservation.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            log.error("Error creating reservation: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to create reservation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get reservation by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID", description = "Retrieve a specific reservation with customer details and confirmation code")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Integer id) {
        log.info("GET request for reservation ID: {}", id);

        Optional<Reservation> reservation = reservationService.getReservationById(id);
        if (reservation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ReservationDto dto = convertToDto(reservation.get());
        return ResponseEntity.ok(dto);
    }

    /**
     * Get all upcoming reservations
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming reservations", description = "Retrieve all upcoming reservations for the current user")
    public ResponseEntity<List<ReservationDto>> getUpcomingReservations() {
        log.info("GET request for upcoming reservations");

        List<ReservationDto> reservations = reservationService.getUpcomingReservations();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get all reservations for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReservationDto>> getCustomerReservations(@PathVariable Integer customerId) {
        log.info("GET request for reservations of customer: {}", customerId);

        List<ReservationDto> reservations = reservationService.getCustomerReservations(customerId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get all reservations for a specific date
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ReservationDto>> getReservationsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("GET request for reservations on date: {}", date);

        List<ReservationDto> reservations = reservationService.getReservationsByDate(date);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Update an existing reservation
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateReservation(
            @PathVariable Integer id,
            @Valid @RequestBody BookingRequestDto bookingRequest) {
        log.info("PUT request to update reservation ID: {}", id);

        try {
            Reservation updated = reservationService.updateReservation(id, bookingRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Reservation updated successfully");
            response.put("reservationId", updated.getId());
            response.put("reservationDate", updated.getReservationDate());
            response.put("reservationTime", updated.getReservationTime());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Cancel a reservation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> cancelReservation(@PathVariable Integer id) {
        log.info("DELETE request to cancel reservation ID: {}", id);

        boolean success = reservationService.cancelReservation(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Reservation cancelled successfully" : "Failed to cancel reservation");

        return ResponseEntity.ok(response);
    }

    /**
     * Update reservation status (e.g., confirmed, seated, no_show, cancelled)
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update reservation status", description = "Update the status of a reservation (pending, confirmed, modified, cancelled, no_show, seated)")
    public ResponseEntity<Map<String, Object>> updateReservationStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> statusUpdate) {
        log.info("PATCH request to update reservation status for ID: {}", id);

        try {
            String status = statusUpdate.get("status");
            if (status == null || status.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Status field is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            boolean success = reservationService.updateReservationStatus(id, status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", "Reservation status updated successfully");
            response.put("reservationId", id);
            response.put("newStatus", status.toLowerCase());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid status update request: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            log.error("Error updating reservation status: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to update reservation status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Check if a table is available for a specific date and time
     */
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestParam Integer tableId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String time,
            @RequestParam Integer partySize) {
        log.info("GET request to check availability - table: {}, date: {}, time: {}", tableId, date, time);

        boolean available = reservationService.isTableAvailable(tableId, date, time, partySize);

        Map<String, Object> response = new HashMap<>();
        response.put("available", available);
        response.put("tableId", tableId);
        response.put("date", date);
        response.put("time", time);
        response.put("partySize", partySize);

        return ResponseEntity.ok(response);
    }

    // Helper method
    private ReservationDto convertToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setCustomerId(reservation.getCustomerId());
        dto.setCustomerName(reservation.getCustomerName());
        dto.setEmail(reservation.getEmail());
        dto.setPhone(reservation.getPhone());
        dto.setTableId(reservation.getTableId());

        // Fetch and set table number
        if (reservation.getTableId() != null) {
            Optional<TableDto> table = tableRepository.findById(reservation.getTableId());
            if (table.isPresent()) {
                dto.setTableNumber(table.get().getTableNumber());
            }
        }

        dto.setPartySize(reservation.getPartySize());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setReservationTime(reservation.getReservationTime());
        dto.setStatus(reservation.getStatus());
        dto.setConfirmationCode(reservation.getConfirmationCode());
        dto.setNotes(reservation.getNotes());
        return dto;
    }

    /**
     * Handle missing request parameters
     */
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParams(
            org.springframework.web.bind.MissingServletRequestParameterException ex) {
        log.warn("Missing request parameter: {}", ex.getParameterName());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "Required parameter '" + ex.getParameterName() + "' is missing");
        errorResponse.put("parameterName", ex.getParameterName());
        errorResponse.put("parameterType", ex.getParameterType());
        errorResponse.put("errorCode", "MISSING_PARAMETER");

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle date format errors
     */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleDateFormatError(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        log.warn("Date format error: parameter={}, value={}, required type={}",
                ex.getName(), ex.getValue(), ex.getRequiredType());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("errorCode", "INVALID_DATE_FORMAT");

        if ("date".equals(ex.getName())) {
            errorResponse.put("message", "Invalid date format for parameter 'date'. Expected format: YYYY-MM-DD (e.g., 2026-03-27). Received: " + ex.getValue());
        } else if ("partySize".equals(ex.getName())) {
            errorResponse.put("message", "Invalid value for parameter 'partySize'. Must be a number between 1 and 20. Received: " + ex.getValue());
        } else {
            errorResponse.put("message", "Invalid value for parameter '" + ex.getName() + "'. Received: " + ex.getValue());
        }

        errorResponse.put("parameterName", ex.getName());
        errorResponse.put("receivedValue", ex.getValue());
        errorResponse.put("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
