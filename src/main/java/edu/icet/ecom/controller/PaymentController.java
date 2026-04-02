package edu.icet.ecom.controller;

import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/payment" ,"/api/payment"})
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public boolean addPayment(@RequestBody PaymentDto paymentDto){
        return addPayment(paymentDto);
    }
}
