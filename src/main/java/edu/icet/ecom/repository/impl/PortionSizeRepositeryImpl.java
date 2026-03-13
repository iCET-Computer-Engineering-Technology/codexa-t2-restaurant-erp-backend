package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.PortionSizeDto;
import edu.icet.ecom.repository.PortionSizeRepositery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PortionSizeRepositeryImpl implements PortionSizeRepositery {

    private final JdbcTemplate template;

    @Override
    public List<PortionSizeDto> getAll() {
        String sql = "SELECT * FROM portion_sizes";
        List<PortionSizeDto> portionSizeDtoList = template.query(sql , (rs, rowNum) -> {
            PortionSizeDto portionSizeDto = new PortionSizeDto();
            portionSizeDto.setId(rs.getInt(1));
            portionSizeDto.setSizeName(rs.getString(2));
            portionSizeDto.setDescription(rs.getString(3));
            portionSizeDto.setCreatedAt(rs.getTimestamp(4));
            return portionSizeDto;
        });
        return portionSizeDtoList;
    }
}
