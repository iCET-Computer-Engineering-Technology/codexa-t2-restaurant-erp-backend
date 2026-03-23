package edu.icet.ecom.repository.impl;

import edu.icet.ecom.repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStatusRepositoryImpl implements OrderStatusRepository {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public void updateOrderStatus(Integer orderId, Integer waiterId, String status) {
        String sql = """
                INSERT INTO order_status_updates (order_id, waiter_id, status)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    status = VALUES(status),
                    updated_at = CURRENT_TIMESTAMP
                """;
        jdbcTemplate.update(sql, orderId, waiterId, status);

    }
}
