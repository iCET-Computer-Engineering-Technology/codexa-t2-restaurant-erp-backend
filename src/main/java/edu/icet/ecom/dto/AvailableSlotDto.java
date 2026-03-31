package edu.icet.ecom.dto;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AvailableSlotDto {
    private LocalTime time;
    private Integer availableTables;
    private Boolean isAvailable;
}

