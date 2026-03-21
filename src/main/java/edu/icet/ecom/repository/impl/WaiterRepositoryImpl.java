package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.repository.WaiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaiterRepositoryImpl implements WaiterRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Waiter> findActiveWaiters() {
        String sql = "SELECT * FROM waiter WHERE status = 'active'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Waiter waiter = new Waiter();
            waiter.setId(rs.getLong("id"));
            waiter.setName(rs.getString("waiter_name"));
            waiter.setStatus(rs.getString("status"));
            return waiter;
        });
    }
}

