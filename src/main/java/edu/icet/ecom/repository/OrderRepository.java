package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Order;

import java.time.LocalDate;

public interface OrderRepository {
    Long saveAndGetId(Order order);
    int upsertSequence(LocalDate date);       // increment sequence
    int getLastSequence(LocalDate date);      // get current value
}

