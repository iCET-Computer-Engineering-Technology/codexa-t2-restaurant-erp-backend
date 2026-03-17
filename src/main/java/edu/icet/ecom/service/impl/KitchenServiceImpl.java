package edu.icet.ecom.service.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.OrderAssignment;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.repository.OrderAssignmentRepository;
import edu.icet.ecom.repository.OrderItemRepository;
import edu.icet.ecom.repository.OrderRepository;
import edu.icet.ecom.repository.WaiterRepository;
import edu.icet.ecom.service.KitchenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenServiceImpl implements KitchenService {

    private final OrderRepository orderRepository;
    private final WaiterRepository waiterRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final OrderItemRepository orderItemRepository;

    public KitchenServiceImpl(OrderRepository orderRepository, WaiterRepository waiterRepository, OrderAssignmentRepository orderAssignmentRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.waiterRepository = waiterRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<Order> getKitchenOrders() {
        List<Order> orders = orderRepository.findOpenOrders();
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
    public void assignWaiter(Integer orderId, Long waiterId) {
        orderAssignmentRepository.assignWaiter(orderId, waiterId);
        orderRepository.updateStatus(orderId, "READY");
    }

    @Override
    public List<OrderAssignment> getAssignments() {
        return orderAssignmentRepository.getAssignments();
    }
}
