package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.CustomerProfileDto;
import edu.icet.ecom.dto.PaymentDto;
import edu.icet.ecom.repository.CustomerRepository;
import edu.icet.ecom.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private static final double POINTS_RATE = 2500.0;

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

    @Override
    public CustomerProfileDto getCustomerProfile(Integer customerId) {
        return customerRepository.getCustomerProfile(customerId);
    }

    @Override
    public void processLoyaltyPoints(PaymentDto paymentDto, Integer customerId) {
        if (paymentDto.getAmount() != null && paymentDto.getAmount() > 0) {
            Double earnedPoints = paymentDto.getAmount() / POINTS_RATE;
            customerRepository.updateLoyaltyPoints(customerId, earnedPoints);
        }
    }
}