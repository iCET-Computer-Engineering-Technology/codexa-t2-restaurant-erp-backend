package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.repository.CustomerRepository;
import edu.icet.ecom.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.getAllCustomers();
    }

    @Override
    public boolean addCustomer(CustomerDto customerDTO) {
        return customerRepository.saveCustomer(customerDTO);
    }

    @Override
    public CustomerDto searchCustomerByPhone(String phone) {
        return customerRepository.searchCustomerByPhone(phone).orElse(null);
    }

    @Override
    public CustomerDto searchCustomerById(Integer id) {
        return customerRepository.searchCustomerById(id).orElse(null);
    }

    @Override
    public boolean deleteCustomerByPhone(String phone) {
        return customerRepository.deleteCustomerByPhone(phone);
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        return customerRepository.updateCustomer(customerDto);
    }
}
