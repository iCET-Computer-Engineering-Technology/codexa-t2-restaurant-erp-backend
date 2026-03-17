package edu.icet.ecom.controller;

import edu.icet.ecom.dto.WaiterOrderDto;
import edu.icet.ecom.entity.Waiters;
import edu.icet.ecom.service.WaitersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiter")
@RequiredArgsConstructor
public class WaiterController {

    private final WaitersService waitersService;

    @GetMapping("/active")
    public List<Waiters> getActiveWaiters() {
        return waitersService.getActiveWaiters();
    }

    @GetMapping("/{waiterId}/unserved")
    public List<WaiterOrderDto> getUnservedOrders(@PathVariable Integer waiterId) {
        return waitersService.getUnservedOrders(waiterId);
    }

    @PutMapping("/serve/{orderId}")
    public boolean serveOrder(@PathVariable Integer orderId) {
        return waitersService.serveOrder(orderId);
    }
}