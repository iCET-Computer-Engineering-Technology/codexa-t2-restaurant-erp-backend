package edu.icet.ecom.controller;

import edu.icet.ecom.dto.OrderRequestDto;
import edu.icet.ecom.dto.OrderResponseDto;
import edu.icet.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDto));
    }

    @GetMapping("/recieved")
    public ResponseEntity<List<OrderResponseDto>> findReceivedOrders(){
        return  ResponseEntity.ok(orderService.findReceivedOrders());
    }
}
