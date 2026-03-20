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
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<OrderDto>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }


    @GetMapping("/find-open-orders")
    public ResponseEntity<List<OrderDto>> findOpenOrders(){
        return ResponseEntity.ok(orderService.findByStatus("open"));
    }

    @GetMapping("/find-by-status/{status}")
    public ResponseEntity<Object> findByStatus(@PathVariable String status) {
        try {
            return ResponseEntity.ok(orderService.findByStatus(status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/update/{orderId}/status")
    ResponseEntity<Object> updateStatus(@PathVariable Integer orderId, @RequestParam String status){
        try {
            orderService.updateStatus(orderId, status);
            return ResponseEntity.ok("Order status updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Integer id){
        return ResponseEntity.ok(orderService.findById(id));
    }
}
