package edu.icet.ecom.repository.impl;

import edu.icet.ecom.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> findPaymentWithUserByOrderId(Integer orderId) {
        String sql = """
            SELECT p.id, p.order_id, p.payment_method, p.amount, 
                   p.tip_amount, p.reference_number, 
                   u.username as processed_by_username,
                   p.processed_at
            FROM payments p
            LEFT JOIN users u ON p.processed_by = u.id
            WHERE p.order_id = ?
            LIMIT 1
            """;
        
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Map<String, Object> result = new HashMap<>();
                result.put("id", rs.getInt("id"));
                result.put("order_id", rs.getInt("order_id"));
                result.put("payment_method", rs.getString("payment_method"));
                result.put("amount", rs.getBigDecimal("amount"));
                result.put("tip_amount", rs.getBigDecimal("tip_amount"));
                result.put("reference_number", rs.getString("reference_number"));
                result.put("processed_by_username", rs.getString("processed_by_username"));
                
                Timestamp processedAt = rs.getTimestamp("processed_at");
                result.put("processed_at", processedAt != null ? processedAt.toLocalDateTime() : null);
                
                return result;
            }, orderId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> findPaymentsByOrderIds(List<Integer> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        String placeholders = String.join(",", Collections.nCopies(orderIds.size(), "?"));
        String sql = String.format("""
            SELECT p.id, p.order_id, p.payment_method, p.amount,
                   p.tip_amount, p.reference_number,
                   u.username as processed_by_username,
                   p.processed_at
            FROM payments p
            LEFT JOIN users u ON p.processed_by = u.id
            WHERE p.order_id IN (%s)
            """, placeholders);
        
        return jdbcTemplate.query(sql, orderIds.toArray(), (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", rs.getInt("id"));
            result.put("order_id", rs.getInt("order_id"));
            result.put("payment_method", rs.getString("payment_method"));
            result.put("amount", rs.getBigDecimal("amount"));
            result.put("tip_amount", rs.getBigDecimal("tip_amount"));
            result.put("reference_number", rs.getString("reference_number"));
            result.put("processed_by_username", rs.getString("processed_by_username"));
            
            Timestamp processedAt = rs.getTimestamp("processed_at");
            result.put("processed_at", processedAt != null ? processedAt.toLocalDateTime() : null);
            
            return result;
        });
    }
}
