package edu.icet.ecom.controller;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDto> getAllCustomer(){
        return customerService.getAllCustomers();
    }

    @PostMapping
    public boolean addCustomer(@Valid @RequestBody CustomerDto customerDto){
        return customerService.addCustomer(customerDto);
    }

    @GetMapping("/phone/{phone}")
    public CustomerDto searchCustomerByPhone(@PathVariable String phone){
        return customerService.searchCustomerByPhone(phone);
    }

    @GetMapping("/{id}")
    public CustomerDto searchCustomerById(@PathVariable Integer id){
        return customerService.searchCustomerById(id);
    }

    @DeleteMapping("/{phone}")
    public boolean deleteCustomerByPhone(@PathVariable String phone){
        return customerService.deleteCustomerByPhone(phone);
    }

    @PutMapping
    public boolean updateCustomer(@Valid @RequestBody CustomerDto customerDto){
        return customerService.updateCustomer(customerDto);
    }
}
