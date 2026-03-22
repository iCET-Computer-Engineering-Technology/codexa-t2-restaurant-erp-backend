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

    @GetMapping("/get-all")
    public List<CustomerDto> getAllCustomer(){
        return customerService.getAllCustomers();
    }

    @PostMapping("/add")
    public boolean addCustomer(@Valid @RequestBody CustomerDto customerDto){
        return customerService.addCustomer(customerDto);
    }

    @GetMapping("/search/phone/{phone}")
    public CustomerDto searchCustomerByPhone(@PathVariable String phone){
        return customerService.searchCustomerByPhone(phone);
    }

    @GetMapping("/search/id/{id}")
    public CustomerDto searchCustomerById(@PathVariable Integer id){
        return customerService.searchCustomerById(id);
    }

    @DeleteMapping("/delete/{phone}")
    public boolean deleteCustomerByPhone(@PathVariable String phone){
        return customerService.deleteCustomerByPhone(phone);
    }

    @PutMapping("/update")
    public boolean updateCustomer(@Valid @RequestBody CustomerDto customerDto){
        return customerService.updateCustomer(customerDto);
    }
}
