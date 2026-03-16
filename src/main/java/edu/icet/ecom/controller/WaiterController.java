package edu.icet.ecom.controller;

import edu.icet.ecom.entity.OrderAssign;
import edu.icet.ecom.service.WaiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiter")
@RequiredArgsConstructor
public class WaiterController {

    private final WaiterService waiterService;

    @GetMapping("/{waiterId}/unserved")
    public List<OrderAssign> getUnservedOrders(@PathVariable Long waiterId) {
        return waiterService.getUnservedOrders(waiterId);
    }

    @PutMapping("/serve/{assignmentId}")
    public boolean serveOrder(@PathVariable Long assignmentId) {
        return waiterService.serveOrder(assignmentId);
    }
}