package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.CustomerDto;
import edu.icet.ecom.dto.CustomerProfileDto;
import edu.icet.ecom.dto.VisitHistoryDto;
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

    /// ////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////

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

    @Override
    public CustomerProfileDto getCustomerProfile(Integer customerId) {
        log.info("Fetching profile for customer ID: {}", customerId);

        // 1. Customer Profile + Lifetime Spend
        String profileSql = """
        SELECT 
            c.id,
            c.first_name,
            c.last_name,
            c.email,
            c.phone,
            c.dietary_notes,
            c.loyalty_points,
            COALESCE(SUM(o.total_amount), 0) AS lifetime_spend
        FROM customers c
        LEFT JOIN orders o ON c.id = o.customer_id
        WHERE c.id = ? 
          AND c.gdpr_deleted = 0
        GROUP BY c.id, c.first_name, c.last_name, c.email, c.phone, 
                 c.dietary_notes, c.loyalty_points
        """;

        CustomerProfileDto profile = jdbcTemplate.queryForObject(
                profileSql,
                (rs, rowNum) -> {
                    CustomerProfileDto dto = new CustomerProfileDto();
                    dto.setCustomerId(rs.getInt("id"));
                    dto.setFirstName(rs.getString("first_name"));
                    dto.setLastName(rs.getString("last_name"));
                    dto.setEmail(rs.getString("email"));
                    dto.setPhone(rs.getString("phone"));
                    dto.setDietaryNotes(rs.getString("dietary_notes"));
                    dto.setLoyaltyPoints(rs.getInt("loyalty_points"));
                    dto.setLifetimeSpend(rs.getDouble("lifetime_spend"));
                    return dto;
                },
                customerId
        );

        String visitsSql = """
        SELECT 
            cv.id AS visit_id,
            cv.visit_date,
            cv.spend_amount,
            cv.notes,
            o.order_number,
            ot.type_name AS order_type,
            o.total_amount,
            o.status
        FROM customer_visits cv
        LEFT JOIN orders o ON cv.order_id = o.id
        LEFT JOIN order_types ot ON o.order_type_id = ot.id
        WHERE cv.customer_id = ?
        ORDER BY cv.visit_date DESC
        LIMIT 10
        """;

        RowMapper<VisitHistoryDto> visitRowMapper = (rs, rowNum) -> {
            VisitHistoryDto visit = new VisitHistoryDto();
            visit.setVisitDate(rs.getTimestamp("visit_date").toLocalDateTime());
            visit.setSpendAmount(rs.getBigDecimal("spend_amount"));
            visit.setNotes(rs.getString("notes"));
            visit.setOrderNumber(rs.getString("order_number"));
            visit.setOrderType(rs.getString("order_type"));
            return visit;
        };

        List<VisitHistoryDto> recentVisits = jdbcTemplate.query(
                visitsSql,
                visitRowMapper,
                customerId
        );

        profile.setRecentVisits(recentVisits);

        log.info("Customer profile fetched successfully for ID: {}. Recent visits: {}",
                customerId, recentVisits.size());

        return profile;
    }
}