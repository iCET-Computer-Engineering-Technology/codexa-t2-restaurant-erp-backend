package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.PortionsDto;
import edu.icet.ecom.repository.PortionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PortionsRepositoryImpl implements PortionsRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean addPortion(PortionsDto portionsDto) {
        return jdbcTemplate.update("INSERT INTO portions (portion_name) VALUES (?)" ,
                portionsDto.getName()
        )>0;
    }

    @Override
    public boolean updatePortion(PortionsDto portionsDto) {
        return jdbcTemplate.update("UPDATE portions SET portion_name = ? WHERE id = ?",
                portionsDto.getName(),
                portionsDto.getId()
        )>0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM portions WHERE id = ?" , id)>0;
    }

    @Override
    public PortionsDto searchById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM portions WHERE id = ?",(rs, rowNum) -> new PortionsDto(
                rs.getInt(1),
                rs.getString(2)
        ) , id);
    }

    @Override
    public List<PortionsDto> getAll() {
        String sql = "SELECT * FROM portions";
        List<PortionsDto> portionsDtoList = jdbcTemplate.query(sql , (rs, rowNum) -> {
            PortionsDto portionDto = new PortionsDto();
            portionDto.setId(rs.getInt(1));
            portionDto.setName(rs.getString(2));
            return portionDto;
        });
        return portionsDtoList;
    }
}

