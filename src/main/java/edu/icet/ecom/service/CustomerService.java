package edu.icet.ecom.service;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.CustomerProfileDto;
import edu.icet.ecom.dto.PaymentDto;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();

    // Backward-compatible alias for existing callers.
    default List<CustomerDto> getAllCustomer() {
        return getAllCustomers();
    }

    boolean addCustomer(CustomerDto customerDto);
    CustomerDto searchCustomerByPhone(String phone);
    CustomerDto searchCustomerById(Integer id);
    boolean deleteCustomerByPhone(String phone);
    boolean updateCustomer(CustomerDto customerDTO);
    CustomerProfileDto getCustomerProfile(Integer customerId);
    void processLoyaltyPoints(PaymentDto paymentDto, Integer customerId);

}