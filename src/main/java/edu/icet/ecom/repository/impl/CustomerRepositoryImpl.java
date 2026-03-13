package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

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
    public List<CustomerDto> getAllCustomer() {
        String sql = "SELECT * FROM customers";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean addCustomer(CustomerDto customerDto) {
        String sql = "INSERT INTO customers (name,email,phone,address) VALUES (?,?,?,?)";
        int result = jdbcTemplate.update(sql,customerDto.getName(),customerDto.getEmail(),customerDto.getPhone(),customerDto.getAddress());
        return result>0;
    }

    @Override
    public CustomerDto searchCustomerByPhone(String phone) {
        String sql = "SELECT * FROM customers WHERE phone = ?";
        List<CustomerDto> customers = jdbcTemplate.query(sql,rowMapper,phone);
        return customers.isEmpty()? null:customers.get(0);
    }

    @Override
    public CustomerDto searchCustomerById(Integer id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        List<CustomerDto> customers = jdbcTemplate.query(sql,rowMapper,id);
        return customers.isEmpty()? null:customers.get(0);
    }

    @Override
    public boolean deleteCustomerByPhone(String phone) {
        String sql = "DELETE FROM customers WHERE phone = ?";
        int result = jdbcTemplate.update(sql,phone);
        return result >0;
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        String sql = "UPDATE customers SET phone=?, name=?, email=?, address=? WHERE id=?";
        int result = jdbcTemplate.update(sql, customerDto.getPhone(),customerDto.getName(), customerDto.getEmail(), customerDto.getAddress(), customerDto.getId());
        return result > 0;
    }
}
