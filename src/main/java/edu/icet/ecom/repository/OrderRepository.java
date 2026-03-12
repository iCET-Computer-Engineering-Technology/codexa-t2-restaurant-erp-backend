package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Order;

import java.time.LocalDate;

public interface OrderRepository {
    Long saveAndGetId(Order order);

    int upsertAndGetSequence(LocalDate date); //for generate order num

}

