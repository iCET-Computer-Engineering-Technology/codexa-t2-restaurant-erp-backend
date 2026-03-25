package edu.icet.ecom.service;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.CustomerProfileDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();

    default List<CustomerDto> getAllCustomer() {
        return getAllCustomers();
    }

    boolean addCustomer(CustomerDto customerDto);
    CustomerDto searchCustomerByPhone(String phone);
    CustomerDto searchCustomerById(Integer id);
    boolean deleteCustomerByPhone(String phone);
    boolean updateCustomer(CustomerDto customerDTO);
}
