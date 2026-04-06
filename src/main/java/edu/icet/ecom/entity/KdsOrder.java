package edu.icet.ecom.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KdsOrder {
    private Integer id;
    private Integer orderId;
    private LocalDateTime displayedAt;
    private LocalDateTime bumpedAt;
    private Integer bumpedBy;
    private Boolean isRush;
    private Boolean isVip;
    private String colorStatus;
}

