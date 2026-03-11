package edu.icet.ecom.service;

import edu.icet.ecom.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomer();
    boolean addCustomer(CustomerDto customerDto);
    CustomerDto searchCustomerByPhone(String phone);
    boolean deleteCustomerByPhone(String phone);
    boolean updateCustomer(CustomerDto customerDTO);
}
