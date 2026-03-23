package edu.icet.ecom.service.impl;

import edu.icet.ecom.repository.KitchenOrderRepository;
import edu.icet.ecom.repository.OrderStatusRepository;
import edu.icet.ecom.service.WaiterServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaiterServiceImpl implements WaiterServcie {

    private final KitchenOrderRepository kitchenOrderRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Override
    public void updateOrderStatus(Integer orderId, Integer waiterId, String status) {
        if (!status.equals("served") && !status.equals("unserved")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        orderStatusRepository.updateOrderStatus(orderId, waiterId, status);
        if (status.equals("served")) {
            kitchenOrderRepository.markAsDone(orderId);
        }
    }

}
