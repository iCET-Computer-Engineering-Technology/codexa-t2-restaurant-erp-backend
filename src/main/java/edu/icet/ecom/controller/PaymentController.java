package edu.icet.ecom.controller;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/payments" ,"/api/payments"})
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public boolean addPayment(@RequestBody PaymentDto paymentDto){
        return paymentService.addPayment(paymentDto);
    }

    @GetMapping
    public List<PaymentDto> getAll(){
        return paymentService.getAllPayment();
    }
}
