package edu.icet.ecom.service.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.OrderAssignment;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.repository.OrderAssignmentRepository;
import edu.icet.ecom.repository.OrderRepository;
import edu.icet.ecom.repository.WaiterRepository;
import edu.icet.ecom.service.KitchenService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenServiceImpl implements KitchenService {

    private final OrderRepository orderRepository;
    private final WaiterRepository waiterRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;

    public KitchenServiceImpl(OrderRepository orderRepository, WaiterRepository waiterRepository, OrderAssignmentRepository orderAssignmentRepository) {
        this.orderRepository = orderRepository;
        this.waiterRepository = waiterRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
    }

    @Override
    public List<Order> getKitchenOrders() {
        return orderRepository.findReceivedOrders();
    }

    @Override
    public List<Waiter> getActiveWaiters() {
        return waiterRepository.findActiveWaiters();
    }

    @Override
    public void assignWaiter(Long orderId, Long waiterId) {

        waiterRepository.assignWaiter(orderId, waiterId);

        orderRepository.updateStatus(orderId, "READY");

    }

    @Override
    public List<OrderAssignment> getAssignments() {
        return orderAssignmentRepository.getAssignments();
    }
}
