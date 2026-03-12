package com.codexa.retauranterp.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderAssigmnetDTO {
    private Long id;
    private Long waiterId;
    private Long orderId;
    private String status;
    private Long tableId;

}
