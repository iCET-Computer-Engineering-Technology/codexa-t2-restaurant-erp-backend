package edu.icet.ecom.service.impl;

import edu.icet.ecom.entity.OrderAssign;
import edu.icet.ecom.repository.WaiterRepository;
import edu.icet.ecom.service.WaiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class WaiterServiceImpl implements WaiterService {

    private final WaiterRepository waiterRepository;

    @Override
    public boolean serveOrder(Long assignmentId) {
        return waiterRepository.markOrderServed(assignmentId);
    }

    @Override
    public List<OrderAssign> getUnservedOrders(Long waiterId) {
        return waiterRepository.getUnservedOrders(waiterId);
    }
}