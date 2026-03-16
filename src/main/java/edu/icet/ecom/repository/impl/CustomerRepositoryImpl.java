package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CustomerDto> rowMapper = (rs, rowNum) -> {
        CustomerDto customer = new CustomerDto();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        return customer;
    };

    @Override
    public List<CustomerDto> getAllCustomers() {
        String sql = "SELECT * FROM customers WHERE gdpr_deleted = 0";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean saveCustomer(CustomerDto customerDto) {
        String sql = "INSERT INTO customers (name,email,phone,address) VALUES (?,?,?,?)";
        int result = jdbcTemplate.update(sql, customerDto.getName(), customerDto.getEmail(), customerDto.getPhone(), customerDto.getAddress());
        return result > 0;
    }


    @Override
    public Optional<CustomerDto> searchCustomerByPhone(String phone) {
        String sql = "SELECT * FROM customers WHERE phone = ? AND gdpr_deleted = 0";
        List<CustomerDto> customers = jdbcTemplate.query(sql, rowMapper, phone);

        return customers.stream().findFirst();
    }

    @Override
    public Optional<CustomerDto> searchCustomerById(Integer id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        List<CustomerDto> customers = jdbcTemplate.query(sql, rowMapper, id);

        return customers.stream().findFirst();
    }

    @Override
    public boolean deleteCustomerByPhone(String phone) {

        Optional<CustomerDto> existingCustomerOpt = searchCustomerByPhone(phone);

        if (existingCustomerOpt.isEmpty()) {
            System.out.println("Validation Failed: Customer not found with phone: " + phone);
            return false;
        }

        java.time.LocalDateTime deleteTime = java.time.LocalDateTime.now();

        String sql = "UPDATE customers SET gdpr_deleted = 1, updated_at = ? WHERE phone = ?";

        int result = jdbcTemplate.update(sql, deleteTime, phone);
        return result > 0;
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        String sql = "UPDATE customers SET name=?, email=?, address=? WHERE phone=?";
        int result = jdbcTemplate.update(sql,  customerDto.getName(), customerDto.getEmail(), customerDto.getAddress(), customerDto.getPhone());
        return result > 0;
    }
}