package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Integer id);
    List<Reservation> findAll();
    List<Reservation> findByCustomerId(Integer customerId);
    List<Reservation> findByReservationDate(LocalDate date);
    List<Reservation> findByReservationDateAndTime(LocalDate date, LocalTime time);
    List<Reservation> findConfirmedReservations(LocalDate date, LocalTime time);
    Reservation update(Reservation reservation);
    boolean updateStatus(Integer id, String status);
    boolean delete(Integer id);
    List<Reservation> findUpcomingReservations();
    boolean existsConflict(Integer tableId, LocalDate date, LocalTime time);
    String generateConfirmationCode();

    /**
     * Find all reservations for a given date where reminder_24h_sent = 0
     */
    List<Reservation> findFor24hReminder(LocalDate date);

    /**
     * Mark the 24h reminder as sent for a reservation
     */
    void mark24hReminderSent(Integer reservationId);
}