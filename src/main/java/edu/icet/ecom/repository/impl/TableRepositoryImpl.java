package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.TableDto;
import edu.icet.ecom.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TableRepositoryImpl implements TableRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<TableDto> findAll() {
        String sql = "SELECT id, table_number, capacity, status FROM `tables` ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TableDto(
                rs.getInt("id"),
                rs.getString("table_number"),
                rs.getInt("capacity"),
                rs.getString("status")
        ));
    }
}
