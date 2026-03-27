package edu.icet.ecom.service;

import edu.icet.ecom.dto.AvailableSlotDto;
import edu.icet.ecom.dto.BookingRequestDto;
import edu.icet.ecom.dto.ReservationDto;
import edu.icet.ecom.entity.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    List<AvailableSlotDto> getAvailableSlots(LocalDate date, Integer partySize);
    Reservation createReservation(BookingRequestDto bookingRequest);
    Optional<Reservation> getReservationById(Integer id);
    List<ReservationDto> getCustomerReservations(Integer customerId);
    List<ReservationDto> getReservationsByDate(LocalDate date);
    List<ReservationDto> getUpcomingReservations();
    Reservation updateReservation(Integer id, BookingRequestDto bookingRequest);
    boolean updateReservationStatus(Integer id, String status);
    boolean cancelReservation(Integer id);
    boolean isTableAvailable(Integer tableId, LocalDate date, String time, Integer partySize);
    List<Integer> getAvailableTablesForDateTime(LocalDate date, String time, Integer partySize);
}
