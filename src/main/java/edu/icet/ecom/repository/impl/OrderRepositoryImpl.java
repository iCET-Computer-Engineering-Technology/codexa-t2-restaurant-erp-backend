package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    //Get open orders
    @Override
    public List<Order> findOpenOrders() {
        return null;
    }

    //Update status
    @Override
    public boolean updateStatus(Integer orderId, String status) {
        return true;
    }

    //Get sequence for order number
    @Override
    public int upsertAndGetSequence(LocalDate date) {
        return 1;
    }

    //Amila
    @Override
    public Integer saveAndGetId(Order order) {
        return 1;
    }
}
