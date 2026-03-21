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

    public class WaiterNameDisplay {
        private String waiter_name;
        private int id;
        private int kitchenOrderId;
        private int waiterId;
        private LocalDateTime assignedAt;
    }


