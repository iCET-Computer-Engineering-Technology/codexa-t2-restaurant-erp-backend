package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.WaiterOrderDto;
import edu.icet.ecom.entity.WaiterOrder;
import edu.icet.ecom.entity.Waiters;
import edu.icet.ecom.repository.impl.WaitersRepositoryImpl;
import edu.icet.ecom.service.WaitersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaitersServiceImpl implements WaitersService {

    private final WaitersRepositoryImpl waitersRepository;

    @Override
    public List<Waiters> getActiveWaiters() {
        return waitersRepository.getActiveWaiters();
    }

    @Override
    public List<WaiterOrderDto> getUnservedOrders(Integer waiterId) {
        return waitersRepository.getUnservedOrders(waiterId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean serveOrder(Integer orderId) {
        return waitersRepository.serveOrder(orderId);
    }
    private WaiterOrderDto toDto(WaiterOrder order) {
        WaiterOrderDto dto = new WaiterOrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setOrderType(order.getOrderType());
        dto.setTableId(order.getTableId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}
