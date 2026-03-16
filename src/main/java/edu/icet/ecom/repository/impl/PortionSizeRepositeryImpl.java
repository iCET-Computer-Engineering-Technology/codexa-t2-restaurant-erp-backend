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
    public boolean addSize(PortionSizeDto portionSizeDto) {
        return template.update("INSERT INTO portion_sizes (size_name, description , created_at)"+"VALUES (?,?,?)" ,
                portionSizeDto.getSizeName(),
                portionSizeDto.getDescription(),
                portionSizeDto.getCreatedAt()
        )>0;
    }

    @Override
    public boolean updateSize(PortionSizeDto portionSizeDto) {
        return template.update("UPDATE portion_sizes SET size_name = ? , description = ? , created_at = ? WHERE portion_size_id = ?",
                portionSizeDto.getSizeName(),
                portionSizeDto.getDescription(),
                portionSizeDto.getCreatedAt(),
                portionSizeDto.getId()
        )>0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return template.update("DELETE FROM portion_sizes WHERE portion_size_id = ?" , id)>1;
    }

    @Override
    public PortionSizeDto searchById(Integer id) {
        return template.queryForObject("SELECT * FROM portion_sizes WHERE portion_size_id = ?",(rs, rowNum) -> new PortionSizeDto(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getTimestamp(4)
        ) , id);
    }

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
