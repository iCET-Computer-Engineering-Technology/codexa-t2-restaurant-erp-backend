package edu.icet.ecom.controller;

import edu.icet.ecom.dto.UpdateOrderStatus;
import edu.icet.ecom.service.WaiterServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import edu.icet.ecom.entity.OrderAssignment;

@RestController
@RequestMapping("/api/waiter")
@RequiredArgsConstructor
public class WaiterController {
    private final WaiterServcie waiterServcie;

    @PostMapping("/status")
    public ResponseEntity<String> updateStatus(@RequestBody UpdateOrderStatus request) {
        waiterServcie.updateOrderStatus(
                request.getOrderId(),
                request.getWaiterId(),
                request.getStatus()
        );
        return ResponseEntity.ok("Status updated to: " + request.getStatus());
    }

    @GetMapping("/assignments/{waiterId}")
    public ResponseEntity<List<OrderAssignment>> getAssignedOrders(@PathVariable Long waiterId) {
        return ResponseEntity.ok(waiterServcie.getAssignedOrders(waiterId));
    }
}