package edu.icet.ecom.controller;

import edu.icet.ecom.dto.AssignWaiterRequest;
import edu.icet.ecom.entity.KitchenOrder;
import edu.icet.ecom.entity.Order;
import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.entity.WaiterDetails;
import edu.icet.ecom.service.KitchenService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kitchen")
public class KitchenController {

    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @GetMapping("/orders")
    public List<KitchenOrder> getOrders() {
        return kitchenService.getKitchenOrders();
    }

    @GetMapping("/waiters")
    public List<Waiter> getWaiters(){

        return kitchenService.getActiveWaiters();
    }

    @GetMapping("/open-orders")
    public List<Order> getOpenOrders() {
        return kitchenService.getOpenOrders();
    }

    @PostMapping("/send")
    public void sendToKitchen(@RequestParam Long orderId) {
        kitchenService.sendToKitchen(orderId);
    }

    @PostMapping("/ready")
    public void markReady(@RequestParam Long orderId) {
        kitchenService.markOrderReady(orderId);
    }

    @PostMapping("/assign-chef")
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('ROLE_CASHIER')")
    public void assignChef(@Valid @RequestBody edu.icet.ecom.dto.AssignChefRequest request) {
        kitchenService.assignChef(request.getKitchenOrderId(), request.getChefId());
    }

    @GetMapping("/chefs")
    @PreAuthorize("hasAnyAuthority('ROLE_CASHIER', 'ROLE_CHEF')")
    public List<edu.icet.ecom.dto.AvailableChefDto> getAvailableChefs() {
        return kitchenService.getAvailableChefs();
    }


    @PostMapping("/assign")
    public void assignWaiter(@Valid @RequestBody AssignWaiterRequest request) {
        if (request.getKitchenOrderId() == null || request.getWaiterId() == null) {
            throw new IllegalArgumentException("kitchenOrderId and waiterId are required");
        }
        kitchenService.assignWaiter(
                request.getKitchenOrderId(),
                request.getWaiterId());
    }

    @GetMapping("/assignments")
    public List<WaiterDetails> getAssignments(){
        return kitchenService.getAssignmentsWaiter();
    }
}
