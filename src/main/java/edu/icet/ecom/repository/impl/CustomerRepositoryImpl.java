package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomerRepositoryImpl implements CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

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
        customer.setBirthday(rs.getDate("birthday") != null ? rs.getDate("birthday").toLocalDate() : null);
        customer.setLoyaltyPoints(rs.getInt("loyalty_points"));
        customer.setCreatedAt(rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null);
        return customer;
    };

    @Override
    public List<CustomerDto> getAllCustomers() {
        String sql = "SELECT * FROM customers WHERE gdpr_deleted = 0";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean saveCustomer(CustomerDto dto) {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone, preferred_language, dietary_notes, communication_email, communication_sms, birthday, loyalty_points) VALUES (?,?,?,?,?,?,?,?,?,?)";
        int result = jdbcTemplate.update(sql,
                dto.getFirstName(), 
                dto.getLastName(), 
                dto.getEmail(), 
                dto.getPhone(),
                dto.getPreferredLanguage() != null ? dto.getPreferredLanguage() : "en",
                dto.getDietaryNotes(),
                dto.getCommunicationEmail() != null ? dto.getCommunicationEmail() : 1,
                dto.getCommunicationSms() != null ? dto.getCommunicationSms() : 1,
                dto.getBirthday(),
                dto.getLoyaltyPoints() != null ? dto.getLoyaltyPoints() : 0
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
        String sql = "UPDATE customers SET gdpr_deleted = 1 WHERE phone = ?";
        return jdbcTemplate.update(sql, phone) > 0;
    }

    @Override
    public boolean updateCustomer(CustomerDto dto) {
        String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, phone=?, preferred_language=?, dietary_notes=?, communication_email=?, communication_sms=?, birthday=?, loyalty_points=? WHERE id = ?";
        int result = jdbcTemplate.update(sql,
                dto.getFirstName(), 
                dto.getLastName(), 
                dto.getEmail(),
                dto.getPhone(),
                dto.getPreferredLanguage(), 
                dto.getDietaryNotes(),
                dto.getCommunicationEmail(), 
                dto.getCommunicationSms(),
                dto.getBirthday(),
                dto.getLoyaltyPoints(),
                dto.getId()
        );
        return result > 0;
    }

    public List<CustomerDto> findCustomersWithBirthdayOn(LocalDate targetDate) {
        String sql = "SELECT * FROM customers " +
                "WHERE gdpr_deleted = 0 " +
                "AND birthday IS NOT NULL " +
                "AND MONTH(birthday) = ? " +
                "AND DAY(birthday) = ?";

        log.debug("Finding customers with birthday on {}/{}", targetDate.getMonthValue(), targetDate.getDayOfMonth());
        return jdbcTemplate.query(sql, rowMapper,
                targetDate.getMonthValue(),
                targetDate.getDayOfMonth());
    }

    @Override
    public List<CustomerDto> findCustomersWithAnniversaryOn(LocalDate targetDate) {
        String sql = "SELECT * FROM customers " +
                "WHERE gdpr_deleted = 0 " +
                "AND created_at IS NOT NULL " +
                "AND MONTH(created_at) = ? " +
                "AND DAY(created_at) = ?";

        log.debug("Finding customers with anniversary on {}/{}", targetDate.getMonthValue(), targetDate.getDayOfMonth());
        return jdbcTemplate.query(sql, rowMapper,
                targetDate.getMonthValue(),
                targetDate.getDayOfMonth());
    }
}