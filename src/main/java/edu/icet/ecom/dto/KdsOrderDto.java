package edu.icet.ecom.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KdsOrderDto {
    private Integer id;
    private Integer orderId;
    private String orderNumber;
    private List<KdsOrderItemDto> items;
    private String colorStatus;
    private Boolean isRush;
    private Boolean isVip;
}

