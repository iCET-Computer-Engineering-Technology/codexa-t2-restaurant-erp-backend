package edu.icet.ecom.repository.impl;

import edu.icet.ecom.repository.KitchenOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KitchenOrderRepositoryImpl implements KitchenOrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void markAsDone(Integer orderId) {
        String sql = """
                UPDATE kitchen_order
                SET status = 'DONE'
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, orderId);
    }
}
