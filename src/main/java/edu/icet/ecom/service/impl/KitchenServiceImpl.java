package edu.icet.ecom.service.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.entity.WaiterDetails;
import edu.icet.ecom.repository.OrderAssignmentRepository;
import edu.icet.ecom.repository.OrderItemRepository;
import edu.icet.ecom.repository.OrderRepository;
import edu.icet.ecom.repository.WaiterRepository;
import edu.icet.ecom.service.KitchenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private final OrderRepository orderRepository;
    private final WaiterRepository waiterRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final OrderItemRepository orderItemRepository;


    @Override
    public List<Order> getKitchenOrders() {
        List<Order> orders = orderRepository.findReceivedOrders();

        orders.forEach(order -> order.setItems(
                orderItemRepository.findByOrderId(order.getId())
        ));

        return orders;
    }

    @Override
    public List<Waiter> getActiveWaiters() {
        return waiterRepository.findActiveWaiters();
    }

    @Override
    public void assignWaiter(Long orderId, Long waiterId) {

        orderAssignmentRepository.assignWaiter(orderId, waiterId);

        orderRepository.updateStatus(orderId, "READY");

    }

    @Override
    public List<WaiterDetails> getAssignments() {
        return orderAssignmentRepository.getAssignments();
    }
}

