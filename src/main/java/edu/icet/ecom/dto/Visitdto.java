package edu.icet.ecom.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Visitdto {
    private Integer id;
    private Integer customerId;
    private LocalDateTime visitDate;
    private Double spendAmount;
    private String notes;
}
