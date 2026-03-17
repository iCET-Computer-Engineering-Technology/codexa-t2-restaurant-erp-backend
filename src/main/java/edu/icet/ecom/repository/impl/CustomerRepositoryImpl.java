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

    // Database එකෙන් එන දත්ත Java Object එකකට ගලපාගන්න තැන
    private final RowMapper<CustomerDto> rowMapper = (rs, rowNum) -> {
        CustomerDto customer = new CustomerDto();
        customer.setId(rs.getInt("id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setPreferredLanguage(rs.getString("preferred_language"));
        customer.setDietaryNotes(rs.getString("dietary_notes"));
        customer.setCommunicationEmail(rs.getInt("communication_email"));
        customer.setCommunicationSms(rs.getInt("communication_sms"));
        customer.setGdprDeleted(rs.getInt("gdpr_deleted"));
        return customer;
    };

    @Override
    public List<CustomerDto> getAllCustomers() {
        String sql = "SELECT * FROM customers WHERE gdpr_deleted = 0";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean saveCustomer(CustomerDto customerDto) {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone, preferred_language, dietary_notes, communication_email, communication_sms) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int result = jdbcTemplate.update(sql,
                customerDto.getFirstName(),
                customerDto.getLastName(),
                customerDto.getEmail(),
                customerDto.getPhone(),
                customerDto.getPreferredLanguage() != null ? customerDto.getPreferredLanguage() : "en",
                customerDto.getDietaryNotes(),
                customerDto.getCommunicationEmail() != null ? customerDto.getCommunicationEmail() : 1,
                customerDto.getCommunicationSms() != null ? customerDto.getCommunicationSms() : 1
        );
        return result > 0;
    }

    @Override
    public Optional<CustomerDto> searchCustomerByPhone(String phone) {
        String sql = "SELECT * FROM customers WHERE phone = ? AND gdpr_deleted = 0";
        return jdbcTemplate.query(sql, rowMapper, phone).stream().findFirst();
    }

    @Override
    public Optional<CustomerDto> searchCustomerById(Integer id) {
        String sql = "SELECT * FROM customers WHERE id = ? AND gdpr_deleted = 0";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    @Override
    public boolean deleteCustomerByPhone(String phone) {
        // Soft Delete: gdpr_deleted එක 1 කරනවා
        String sql = "UPDATE customers SET gdpr_deleted = 1, updated_at = CURRENT_TIMESTAMP WHERE phone = ?";
        return jdbcTemplate.update(sql, phone) > 0;
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, preferred_language=?, dietary_notes=?, communication_email=?, communication_sms=? WHERE phone=?";

        int result = jdbcTemplate.update(sql,
                customerDto.getFirstName(),
                customerDto.getLastName(),
                customerDto.getEmail(),
                customerDto.getPreferredLanguage(),
                customerDto.getDietaryNotes(),
                customerDto.getCommunicationEmail(),
                customerDto.getCommunicationSms(),
                customerDto.getPhone()
        );
        return result > 0;
    }
}