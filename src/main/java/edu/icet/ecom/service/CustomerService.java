package edu.icet.ecom.service;

import edu.icet.ecom.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();
    boolean saveCustomer(CustomerDto customerDto);
    CustomerDto searchCustomerByPhone(String phone);
    CustomerDto searchCustomerById(Integer id);
    boolean deleteCustomerByPhone(String phone);
    boolean updateCustomer(CustomerDto customerDTO);
}
