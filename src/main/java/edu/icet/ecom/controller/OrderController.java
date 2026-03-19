package edu.icet.ecom.controller;

import edu.icet.ecom.dto.OrderDto;
import edu.icet.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody OrderDto orderDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Integer id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/find-by-status/{status}")
    public ResponseEntity<List<OrderDto>> findByStatus(@PathVariable String status) {
        return null;
    }
    @GetMapping("/open-orders")
    public ResponseEntity<List<OrderDto>> findOpenOrders(){
        return  null;
    }

    @PutMapping("/update/{orderId}/status")
    ResponseEntity<String> updateStatus(@PathVariable Integer orderId, @RequestParam String status){
        return null;
    }
}
