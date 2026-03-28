package edu.icet.ecom.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reservation {
    private Integer id;
    private Integer customerId;
    private String customerName;
    private String email;
    private String phone;
    private Integer tableId;
    private Integer partySize;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String status;
    private String confirmationCode;
    private Integer reminder24hSent;
    private Integer reminder2hSent;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


