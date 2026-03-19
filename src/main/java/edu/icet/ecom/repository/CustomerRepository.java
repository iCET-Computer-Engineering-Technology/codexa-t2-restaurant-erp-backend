package edu.icet.ecom.repository;

import edu.icet.ecom.dto.CustomerDto;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    List<CustomerDto> getAllCustomers();
    boolean saveCustomer(CustomerDto customerDto);
    Optional<CustomerDto> searchCustomerByPhone(String phone);
    Optional<CustomerDto> searchCustomerById(Integer id);
    boolean deleteCustomerByPhone(String phone);
    boolean updateCustomer(CustomerDto customerDTO);
}

