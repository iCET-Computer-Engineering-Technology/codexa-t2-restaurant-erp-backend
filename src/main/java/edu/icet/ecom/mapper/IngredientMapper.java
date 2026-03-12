package edu.icet.ecom.mapper;

import edu.icet.ecom.dto.IngredientDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientMapper implements RowMapper<IngredientDto> {

    @Override
    public  IngredientDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new IngredientDto(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("quantity"),
                rs.getDouble("price"),
                rs.getString("description")
        );
    }
}
