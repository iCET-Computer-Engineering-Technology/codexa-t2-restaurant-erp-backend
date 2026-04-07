package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.TableDto;
import edu.icet.ecom.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<TableDto> findById(Integer id) {
        String sql = "SELECT id, table_number, capacity, status FROM `tables` WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TableDto(
                rs.getInt("id"),
                rs.getString("table_number"),
                rs.getInt("capacity"),
                rs.getString("status")
        ), id).stream().findFirst();
    }

    @Override
    public void updateStatus(Integer tableId, String status) {
        String sql = "UPDATE `tables` SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        jdbcTemplate.update(sql, status, tableId);
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM `tables` WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
