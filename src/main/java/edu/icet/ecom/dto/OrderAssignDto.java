package edu.icet.ecom.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderAssignDto {
    private Long id;
    private Long waiterId;
    private Long orderId;
    private String status;
    private Long tableId;
}
