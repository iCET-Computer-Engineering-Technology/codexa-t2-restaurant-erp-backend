package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.AvailableSlotDto;
import edu.icet.ecom.dto.BookingRequestDto;
import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.ReservationDto;
import edu.icet.ecom.entity.Reservation;
import edu.icet.ecom.repository.CustomerRepository;
import edu.icet.ecom.repository.ReservationRepository;
import edu.icet.ecom.repository.TableRepository;
import edu.icet.ecom.service.EmailService;
import edu.icet.ecom.service.EmailTemplateService;
import edu.icet.ecom.service.ReservationService;
import edu.icet.ecom.service.TableManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final TableRepository tableRepository;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final TableManagementService tableManagementService;

    private static final int BUSINESS_HOURS_START = 11; // 11:00 AM
    private static final int BUSINESS_HOURS_END = 22;   // 10:00 PM
    private static final int SLOT_INTERVAL_MINUTES = 30; // 30-minute slots

    @Override
    public List<AvailableSlotDto> getAvailableSlots(LocalDate date, Integer partySize) {
        log.info("Getting available slots for date: {}, party size: {}", date, partySize);

        List<AvailableSlotDto> availableSlots = new ArrayList<>();

        // Generate time slots for the day
        for (int hour = BUSINESS_HOURS_START; hour < BUSINESS_HOURS_END; hour++) {
            for (int minute = 0; minute < 60; minute += SLOT_INTERVAL_MINUTES) {
                LocalTime slotTime = LocalTime.of(hour, minute);

                // Get available tables for this slot
                List<Integer> availableTables = getAvailableTablesForDateTime(date, slotTime.toString(), partySize);

                AvailableSlotDto slot = new AvailableSlotDto();
                slot.setTime(slotTime);
                slot.setAvailableTables(availableTables.size());
                slot.setIsAvailable(!availableTables.isEmpty());

                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }

    @Override
    public Reservation createReservation(BookingRequestDto bookingRequest) {
        log.info("Creating reservation for customer: {}, date: {}, time: {}, party size: {}",
                bookingRequest.getCustomerName(), bookingRequest.getReservationDate(),
                bookingRequest.getReservationTime(), bookingRequest.getPartySize());

        // Step 1: Validate input
        if (bookingRequest.getReservationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Reservation date cannot be in the past");
        }

        Integer selectedTableId = bookingRequest.getTableId();
        if (selectedTableId == null) {
            List<Integer> availableTables = getAvailableTablesForDateTime(
                    bookingRequest.getReservationDate(),
                    bookingRequest.getReservationTime().toString(),
                    bookingRequest.getPartySize()
            );
            if (availableTables.isEmpty()) {
                throw new IllegalArgumentException("No tables are available for the selected date/time and party size");
            }
            selectedTableId = availableTables.get(0);
        }

        // Step 2: Check for table availability (conflict prevention)
        if (reservationRepository.existsConflict(selectedTableId,
                bookingRequest.getReservationDate(), bookingRequest.getReservationTime())) {
            throw new IllegalArgumentException("Selected table is already booked for this time slot");
        }

        // Step 3: Get or create customer
        Integer customerId = getOrCreateCustomer(bookingRequest);

        // Step 4: Create reservation
        Reservation reservation = new Reservation();
        reservation.setCustomerId(customerId);
        reservation.setCustomerName(bookingRequest.getCustomerName());
        reservation.setEmail(bookingRequest.getEmail());
        reservation.setPhone(bookingRequest.getPhone());
        reservation.setTableId(selectedTableId);
        reservation.setPartySize(bookingRequest.getPartySize());
        reservation.setReservationDate(bookingRequest.getReservationDate());
        reservation.setReservationTime(bookingRequest.getReservationTime());
        reservation.setStatus("confirmed");
        reservation.setConfirmationCode(reservationRepository.generateConfirmationCode());
        reservation.setReminder24hSent(0);
        reservation.setReminder2hSent(0);
        reservation.setNotes(bookingRequest.getNotes());

        Reservation savedReservation = reservationRepository.save(reservation);

        // Update table status to reserved
        try {
            tableManagementService.updateTableStatusAutomatic(selectedTableId, "reserved", "RESERVATION_MADE");
        } catch (Exception e) {
            log.error("Failed to update table status to reserved: {}", e.getMessage());
            // Non-blocking: reservation creation succeeds even if table update fails
        }

        // Send confirmation email (non-blocking for reservation flow)
        try {
            if (savedReservation.getEmail() != null && !savedReservation.getEmail().trim().isEmpty()) {
                log.info("Preparing confirmation email for reservation {} to {}", savedReservation.getId(), savedReservation.getEmail());
                // Reuse reservation reminder template for confirmation until a dedicated confirmation template is added
                String template = emailTemplateService.getReservationReminderEmailTemplate();
                String body = buildReservationEmailBody(savedReservation, template);
                sendEmailWithRetry(savedReservation, body);
                log.info("Confirmation email triggered for reservation {}", savedReservation.getId());
            } else {
                log.warn("Skipping confirmation email - no customer email for reservation {}", savedReservation.getId());
            }
        } catch (Exception e) {
            // Do not fail reservation if email sending fails
            log.error("Failed to send reservation confirmation email for reservation {}: {}", savedReservation.getId(), e.getMessage(), e);
        }

        log.info("Reservation created successfully with confirmation code: {}", savedReservation.getConfirmationCode());

        return savedReservation;
    }

    @Override
    public Optional<Reservation> getReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<ReservationDto> getCustomerReservations(Integer customerId) {
        return reservationRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> getReservationsByDate(LocalDate date) {
        return reservationRepository.findByReservationDate(date).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDto> getUpcomingReservations() {
        return reservationRepository.findUpcomingReservations().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation updateReservation(Integer id, BookingRequestDto bookingRequest) {
        log.info("Updating reservation with ID: {}", id);

        Optional<Reservation> existingOpt = reservationRepository.findById(id);
        if (!existingOpt.isPresent()) {
            throw new IllegalArgumentException("Reservation not found with ID: " + id);
        }

        Reservation existing = existingOpt.get();
        Integer targetTableId = bookingRequest.getTableId() != null ? bookingRequest.getTableId() : existing.getTableId();

        // Check for conflicts if table or time changed
        if (!existing.getTableId().equals(targetTableId) ||
                !existing.getReservationDate().equals(bookingRequest.getReservationDate()) ||
                !existing.getReservationTime().equals(bookingRequest.getReservationTime())) {

            if (reservationRepository.existsConflict(targetTableId,
                    bookingRequest.getReservationDate(), bookingRequest.getReservationTime())) {
                throw new IllegalArgumentException("Selected table is already booked for this time slot");
            }
        }

        // Update fields
        existing.setTableId(targetTableId);
        existing.setPartySize(bookingRequest.getPartySize());
        existing.setReservationDate(bookingRequest.getReservationDate());
        existing.setReservationTime(bookingRequest.getReservationTime());
        existing.setNotes(bookingRequest.getNotes());

        return reservationRepository.update(existing);
    }

    @Override
    public boolean cancelReservation(Integer id) {
        log.info("Cancelling reservation with ID: {}", id);
        return reservationRepository.delete(id);
    }

    @Override
    public boolean updateReservationStatus(Integer id, String status) {
        log.info("Updating reservation status for ID: {}, new status: {}", id, status);

        // Validate status
        String[] validStatuses = {"pending", "confirmed", "modified", "cancelled", "no_show", "seated"};
        boolean isValidStatus = false;
        for (String validStatus : validStatuses) {
            if (validStatus.equals(status.toLowerCase())) {
                isValidStatus = true;
                break;
            }
        }

        if (!isValidStatus) {
            log.warn("Invalid status provided: {}", status);
            throw new IllegalArgumentException("Invalid status. Valid statuses are: pending, confirmed, modified, cancelled, no_show, seated");
        }

        // Check if reservation exists
        Optional<Reservation> existing = reservationRepository.findById(id);
        if (existing.isEmpty()) {
            log.warn("Reservation not found with ID: {}", id);
            throw new IllegalArgumentException("Reservation not found with ID: " + id);
        }

        return reservationRepository.updateStatus(id, status.toLowerCase());
    }

    @Override
    public boolean isTableAvailable(Integer tableId, LocalDate date, String time, Integer partySize) {
        LocalTime localTime = LocalTime.parse(time);
        return !reservationRepository.existsConflict(tableId, date, localTime);
    }

    @Override
    public List<Integer> getAvailableTablesForDateTime(LocalDate date, String time, Integer partySize) {
        LocalTime localTime = LocalTime.parse(time);

        // Get all tables with sufficient capacity
        List<Integer> allTables = tableRepository.findAll().stream()
                .filter(t -> t.getCapacity() != null && t.getCapacity() >= partySize)
                .map(t -> t.getId())
                .collect(Collectors.toList());

        // Filter out tables with conflicts
        return allTables.stream()
                .filter(tableId -> !reservationRepository.existsConflict(tableId, date, localTime))
                .collect(Collectors.toList());
    }

    // Helper methods
    private Integer getOrCreateCustomer(BookingRequestDto bookingRequest) {
        // Try to find existing customer by phone
        Optional<CustomerDto> existingCustomer = customerRepository.searchCustomerByPhone(bookingRequest.getPhone());

        if (existingCustomer.isPresent()) {
            return existingCustomer.get().getId();
        }

        // Create new customer
        CustomerDto newCustomer = new CustomerDto();
        newCustomer.setFirstName(bookingRequest.getCustomerName().split(" ")[0]);
        newCustomer.setLastName(bookingRequest.getCustomerName().contains(" ") ?
                bookingRequest.getCustomerName().substring(bookingRequest.getCustomerName().indexOf(" ") + 1) : "");
        newCustomer.setEmail(bookingRequest.getEmail());
        newCustomer.setPhone(bookingRequest.getPhone());
        newCustomer.setPreferredLanguage("en");
        newCustomer.setCommunicationEmail(1);
        newCustomer.setCommunicationSms(1);

        if (customerRepository.saveCustomer(newCustomer)) {
            Optional<CustomerDto> savedCustomer = customerRepository.searchCustomerByPhone(bookingRequest.getPhone());
            return savedCustomer.map(CustomerDto::getId).orElseThrow(() -> new RuntimeException("Failed to create customer"));
        }

        throw new RuntimeException("Failed to create or retrieve customer");
    }

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
            Optional<edu.icet.ecom.dto.TableDto> table = tableRepository.findById(reservation.getTableId());
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




    private String safe(String value) {
        return value == null ? "" : value;
    }


    private String buildReservationEmailBody(Reservation reservation, String emailTemplate) {
        log.info("Building email body for reservation: {}", reservation.getId());

        // Get table number
        String tableNumber = "TBD";
        if (reservation.getTableId() != null) {
            Optional<edu.icet.ecom.dto.TableDto> table = tableRepository.findById(reservation.getTableId());
            if (table.isPresent()) {
                tableNumber = table.get().getTableNumber();
            }
        }

        // Prepare placeholders
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("customerName", safe(reservation.getCustomerName()));
        placeholders.put("confirmationCode", safe(reservation.getConfirmationCode()));
        placeholders.put("reservationDate", reservation.getReservationDate() != null ?
                reservation.getReservationDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) : "N/A");
        placeholders.put("reservationTime", reservation.getReservationTime() != null ?
                reservation.getReservationTime().format(DateTimeFormatter.ofPattern("h:mm a")) : "N/A");
        placeholders.put("tableNumber", tableNumber);
        placeholders.put("partySize", reservation.getPartySize() != null ?
                reservation.getPartySize().toString() : "N/A");
        placeholders.put("partyLabel", (reservation.getPartySize() != null && reservation.getPartySize() > 1) ?
                "people" : "person");
        placeholders.put("status", safe(reservation.getStatus()).toUpperCase());
        placeholders.put("customerEmail", safe(reservation.getEmail()));
        placeholders.put("customerPhone", safe(reservation.getPhone()));

        // Add notes section if available
        if (reservation.getNotes() != null && !reservation.getNotes().trim().isEmpty()) {
            placeholders.put("notesSection",
                    "<div class=\"details-section\">\n" +
                            "    <div class=\"section-title\">📝 Special Requests</div>\n" +
                            "    <div class=\"detail-row\">\n" +
                            "        <span class=\"detail-value\">" + reservation.getNotes() + "</span>\n" +
                            "    </div>\n" +
                            "</div>");
        } else {
            placeholders.put("notesSection", "");
        }

        // Restaurant contact info
        placeholders.put("phone", "+1 (555) 123-4567");
        placeholders.put("email", "support@restauranterp.com");
        placeholders.put("website", "www.restauranterp.com");

        // Render template
        return emailTemplateService.renderTemplate(emailTemplate, placeholders);
    }

//    private void sendReservationConfirmationEmail(Reservation reservation) {
//        log.info(">>> STARTING EMAIL COMPOSITION <<<");
//        log.info("Customer Email: {}", reservation.getEmail());
//        log.info("Customer Name: {}", reservation.getCustomerName());
//
//        try {
//            // Validate email address
//            if (reservation.getEmail() == null || reservation.getEmail().trim().isEmpty()) {
//                log.error("!!! EMAIL ADDRESS IS NULL OR EMPTY !!!");
//                throw new IllegalArgumentException("Customer email address is missing");
//            }
//
//            log.info("Step 1: Fetching table number...");
//            // Get table number
//            String tableNumber = "TBD";
//            if (reservation.getTableId() != null) {
//                Optional<edu.icet.ecom.dto.TableDto> table = tableRepository.findById(reservation.getTableId());
//                if (table.isPresent()) {
//                    tableNumber = table.get().getTableNumber();
//                    log.info("✓ Table number found: {}", tableNumber);
//                } else {
//                    log.warn("⚠ Table ID {} not found in database", reservation.getTableId());
//                }
//            } else {
//                log.warn("⚠ Reservation has no table ID assigned");
//            }
//
//            log.info("Step 2: Preparing email placeholders...");
//            // Prepare email placeholders
//            Map<String, String> placeholders = new HashMap<>();
//            placeholders.put("customerName", safe(reservation.getCustomerName()));
//            placeholders.put("confirmationCode", safe(reservation.getConfirmationCode()));
//            placeholders.put("reservationDate", reservation.getReservationDate() != null ?
//                reservation.getReservationDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) : "N/A");
//            placeholders.put("reservationTime", reservation.getReservationTime() != null ?
//                reservation.getReservationTime().format(DateTimeFormatter.ofPattern("h:mm a")) : "N/A");
//            placeholders.put("tableNumber", tableNumber);
//            placeholders.put("partySize", reservation.getPartySize() != null ?
//                reservation.getPartySize().toString() : "N/A");
//            placeholders.put("partyLabel", (reservation.getPartySize() != null && reservation.getPartySize() > 1) ?
//                "people" : "person");
//            placeholders.put("status", safe(reservation.getStatus()).toUpperCase());
//            placeholders.put("customerEmail", safe(reservation.getEmail()));
//            placeholders.put("customerPhone", safe(reservation.getPhone()));
//
//            log.info("✓ Placeholders prepared");
//
//            // Add notes section if available
//            if (reservation.getNotes() != null && !reservation.getNotes().trim().isEmpty()) {
//                placeholders.put("notesSection",
//                    "<div class=\"details-section\">\n" +
//                    "    <div class=\"section-title\">📝 Special Requests</div>\n" +
//                    "    <div class=\"detail-row\">\n" +
//                    "        <span class=\"detail-value\">" + reservation.getNotes() + "</span>\n" +
//                    "    </div>\n" +
//                    "</div>");
//                log.info("✓ Notes section included");
//            } else {
//                placeholders.put("notesSection", "");
//            }
//
//            // Restaurant contact info
//            placeholders.put("phone", "+1 (555) 123-4567");
//            placeholders.put("email", "support@restauranterp.com");
//            placeholders.put("website", "www.restauranterp.com");
//
//            log.info("Step 3: Getting email template...");
//            // Get email template
//            String emailTemplate = emailTemplateServiceng s.getReservationConfirmationEmailTemplate();
//            if (emailTemplate == null || emailTemplate.isEmpty()) {
//                log.error("!!! EMAIL TEMPLATE IS NULL OR EMPTY !!!");
//                throw new RuntimeException("Email template is not available");
//            }
//            log.info("✓ Email template retrieved (length: {} chars)", emailTemplate.length());
//
//            log.info("Step 4: Rendering template with placeholders...");
//            // Render template with placeholders
//            String emailBody = emailTemplateService.renderTemplate(emailTemplate, placeholders);
//            if (emailBody == null || emailBody.isEmpty()) {
//                log.error("!!! RENDERED EMAIL BODY IS NULL OR EMPTY !!!");
//                throw new RuntimeException("Email body rendering failed");
//            }
//            log.info("✓ Email body rendered (length: {} chars)", emailBody.length());
//
//            log.info("Step 5: Sending email via EmailService...");
//            log.info("TO: {}", reservation.getEmail());
//            log.info("SUBJECT: Reservation Confirmation - {}", reservation.getConfirmationCode());
//
//            // Send email with retry logic
//            sendEmailWithRetry(reservation, emailBody);
//
//            log.info(">>> ✓ CONFIRMATION EMAIL SENT SUCCESSFULLY TO: {} <<<", reservation.getEmail());
//        } catch (Exception e) {
//            log.error(">>> !!! ERROR SENDING CONFIRMATION EMAIL !!! <<<");
//            log.error("Email address: {}", reservation.getEmail());
//            log.error("Error message: {}", e.getMessage());
//            log.error("Error type: {}", e.getClass().getName());
//            // Don't throw - log and continue
//        }
//    }

    private void sendEmailWithRetry(Reservation reservation, String emailBody) {
        int maxRetries = 3;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                // Send email
                emailService.sendEmailToCustomer(
                        reservation.getEmail(),
                        "Reservation Confirmation - " + reservation.getConfirmationCode(),
                        emailBody
                );
                return; // Success
            } catch (Exception e) {
                retryCount++;
                if (retryCount < maxRetries) {
                    int delaySeconds = (int) Math.pow(2, retryCount);
                    log.warn("⚠ Email send failed (Attempt {}/{}). Retrying in {} seconds...", retryCount, maxRetries, delaySeconds);
                    try {
                        Thread.sleep(delaySeconds * 1000L);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("Thread interrupted during retry delay");
                        break;
                    }
                } else {
                    log.error("⚠ Email sending failed after {} attempts. This is likely a network issue.", maxRetries);
                    log.error("Reservation: {}, Email: {}", reservation.getId(), reservation.getEmail());
                    log.error("The email will be retried automatically by the background service.");
                }
            }
        }
    }

    @Scheduled(cron = "0 3 9 * * *")
    public void send24hReminders() {
        send24hReminders(null);
    }

    // Remove @Scheduled from this method, as it does not exist in the interface
    public void send24hReminders(LocalDate date) {
        LocalDate reminderDate = (date != null) ? date : LocalDate.now().plusDays(1);
        List<Reservation> reservations = reservationRepository.findFor24hReminder(reminderDate);
        for (Reservation reservation : reservations) {
            try {
                if (reservation.getEmail() != null && !reservation.getEmail().isBlank()) {
                    String subject = "Reservation Reminder - " + reservation.getReservationDate();
                    String body = buildReservationEmailBody(reservation, emailTemplateService.getReservationReminderEmailTemplate());
                    emailService.sendEmailToCustomer(reservation.getEmail(), subject, body);
                    reservationRepository.mark24hReminderSent(reservation.getId());
                    log.info("24h reminder sent for reservation {} to {}", reservation.getId(), reservation.getEmail());
                }
            } catch (Exception e) {
                log.error("Failed to send 24h reminder for reservation {}: {}", reservation.getId(), e.getMessage());
            }
        }
    }
}
