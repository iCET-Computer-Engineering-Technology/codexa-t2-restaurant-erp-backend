package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.OrderTypeDto;
import edu.icet.ecom.repository.OrderTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderTypeRepositoryImpl implements OrderTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<OrderTypeDto> rowMapper = new RowMapper<OrderTypeDto>() {
        @Override
        public OrderTypeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderTypeDto dto = new OrderTypeDto();
            dto.setId(rs.getInt("id"));
            dto.setTypeName(rs.getString("type_name"));
            dto.setDescription(rs.getString("description"));
            dto.setIsActive(rs.getBoolean("is_active"));
            dto.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
            dto.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
            return dto;
        }
    };

    @Override
    public List<OrderTypeDto> findAll() {
        String sql = "SELECT * FROM order_types";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public OrderTypeDto findById(Integer id) {
        String sql = "SELECT * FROM order_types WHERE id = ?";
        List<OrderTypeDto> results = jdbcTemplate.query(sql, new Object[]{id}, rowMapper);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public OrderTypeDto findByTypeName(String typeName) {
        String sql = "SELECT * FROM order_types WHERE type_name = ?";
        List<OrderTypeDto> results = jdbcTemplate.query(sql, new Object[]{typeName}, rowMapper);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<OrderTypeDto> findAllActive() {
        String sql = "SELECT * FROM order_types WHERE is_active = 1";
        return jdbcTemplate.query(sql, rowMapper);
    }
}

