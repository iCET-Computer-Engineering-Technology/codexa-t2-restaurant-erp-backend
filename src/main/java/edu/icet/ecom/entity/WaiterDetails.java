package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor

    public class WaiterDetails {
        private String waiter_name;
        private Integer id;
        private Integer kitchenOrderId;
        private Integer waiterId;
        private LocalDateTime assignedAt;
    }


