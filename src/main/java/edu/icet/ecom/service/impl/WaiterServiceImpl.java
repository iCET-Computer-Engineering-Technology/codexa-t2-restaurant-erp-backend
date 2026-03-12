package edu.icet.ecom.service.impl;

import com.codexa.retauranterp.service.WaiterService;
import edu.icet.ecom.entity.OrderAssigment;
import edu.icet.ecom.repository.WaiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class WaiterServiceImpl implements WaiterService {

    private final WaiterRepository waiterRepository;

    @Override
    public List<OrderAssigment> getUnservedOrders() {
        return waiterRepository.getUnservedOrders();
    }
}