package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.TableDto;
import edu.icet.ecom.dto.TablePositionDto;
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
        return jdbcTemplate.query(sql, (rs, rowNum) ->{
                TableDto tableDto = new TableDto();
                tableDto.setTableNumber(rs.getString("table_number"));
               tableDto.setCapacity( rs.getInt("capacity"));
                tableDto.setStatus(rs.getString("status"));
                return tableDto;
        });
    }

    @Override
    public Optional<TableDto> findById(Integer id) {
        String sql = "SELECT id, table_number, capacity, status FROM `tables` WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->{
            TableDto tableDto = new TableDto();
            tableDto.setTableNumber(rs.getString("table_number"));
            tableDto.setCapacity( rs.getInt("capacity"));
            tableDto.setStatus(rs.getString("status"));
            return tableDto;
        }, id).stream().findFirst();
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

    @Override
    public List<TablePositionDto> findAllWithPositions() {
        String sql = "SELECT id, table_number, capacity, section_id, pos_x, pos_y, status, updated_at FROM `tables` ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TablePositionDto(
                rs.getInt("id"),
                rs.getString("table_number"),
                rs.getInt("capacity"),
                (Integer) rs.getObject("section_id"),
                (Integer) rs.getObject("pos_x"),
                (Integer) rs.getObject("pos_y"),
                rs.getString("status"),
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        ));
    }

    @Override
    public List<TablePositionDto> findBySectionId(Integer sectionId) {
        String sql = "SELECT id, table_number, capacity, section_id, pos_x, pos_y, status, updated_at FROM `tables` WHERE section_id = ? ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TablePositionDto(
                rs.getInt("id"),
                rs.getString("table_number"),
                rs.getInt("capacity"),
                (Integer) rs.getObject("section_id"),
                (Integer) rs.getObject("pos_x"),
                (Integer) rs.getObject("pos_y"),
                rs.getString("status"),
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        ), sectionId);
    }

    @Override
    public void updateTablePosition(Integer tableId, Integer sectionId, Integer posX, Integer posY) {
        String sql = "UPDATE `tables` SET section_id = ?, pos_x = ?, pos_y = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        jdbcTemplate.update(sql, sectionId, posX, posY, tableId);
    }
}
