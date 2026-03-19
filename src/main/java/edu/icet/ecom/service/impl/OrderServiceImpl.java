package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.OrderRequestDto;
import edu.icet.ecom.dto.OrderResponseDto;
import edu.icet.ecom.repository.*;
import edu.icet.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // Valid order types matching the DB ENUM
    private static final Set<String> VALID_ORDER_TYPES = Set.of("dine_in", "takeout", "delivery", "online");

    // Valid statuses matching the DB ENUM
    private static final List<String> VALID_STATUSES = List.of("open", "sent_to_kitchen", "partially_ready", "ready", "paid", "voided");

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
       return null;
    }

    @Override
    public List<OrderResponseDto> findOpenOrders() {
        return null;
    }

    @Override
    public Boolean updateStatus(Integer orderId, String status) {
        return true;
    }


    //Generate order num : ORD-20260318-0001
    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();
        int sequence = orderRepository.upsertAndGetSequence(today);
        String date = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + date + "-" + String.format("%04d", sequence);
    }
}
