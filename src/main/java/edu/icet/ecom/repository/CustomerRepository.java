package edu.icet.ecom.repository;

import edu.icet.ecom.dto.CustomerDto;
import java.util.List;

public interface CustomerRepository {
    List<CustomerDto> getAllCustomer();
    boolean addCustomer(CustomerDto customerDto);
    CustomerDto searchCustomerByPhone(String phone);
    CustomerDto searchCustomerById(Integer id);
    boolean deleteCustomerByPhone(String phone);
    boolean updateCustomer(CustomerDto customerDTO);
}

