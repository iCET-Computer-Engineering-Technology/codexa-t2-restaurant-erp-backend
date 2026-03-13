package edu.icet.ecom.service.impl;

import edu.icet.ecom.entity.OrderAssigment;
import edu.icet.ecom.repository.WaiterRepository;
import edu.icet.ecom.service.WaiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class WaiterServiceImpl implements edu.icet.ecom.service.WaiterService {

    private final WaiterRepository waiterRepository;

    @Override
    public List<OrderAssigment> getUnservedOrders() {
        return waiterRepository.getUnservedOrders();
    }

    @Override
    public boolean serveOrder(Long orderId) {
        return waiterRepository.markOrderServed(orderId);
    }
}