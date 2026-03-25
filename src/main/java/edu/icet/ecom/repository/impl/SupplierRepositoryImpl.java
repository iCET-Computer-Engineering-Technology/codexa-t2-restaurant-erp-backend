package edu.icet.ecom.repository.impl;

import edu.icet.ecom.dto.SupplierDto;
import edu.icet.ecom.entity.Supplier;
import edu.icet.ecom.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SupplierRepositoryImpl implements SupplierRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Supplier> getAllSuppliers() {
        String query = "SELECT * FROM suppliers";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Supplier supplier = new Supplier();
            supplier.setId(rs.getInt("id"));
            supplier.setName(rs.getString("name"));
            supplier.setContactName(rs.getString("contact_name"));
            supplier.setEmail(rs.getString("email"));
            supplier.setPhone(rs.getString("phone"));
            supplier.setAddress(rs.getString("address"));
            return supplier;
        });
    }

    @Override
    public SupplierDto saveSupplier(SupplierDto supplierDto) {
        String sql = "INSERT INTO suppliers (name, contact_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, supplierDto.getName());
            ps.setString(2, supplierDto.getContactName());
            ps.setString(3, supplierDto.getEmail());
            ps.setString(4, supplierDto.getPhone());
            ps.setString(5, supplierDto.getAddress());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            supplierDto.setId(key.intValue());
        }

        return supplierDto;
    }

    @Override
    public SupplierDto updateSupplier(SupplierDto supplierDto) {
        String updateSql = "UPDATE suppliers SET name=?, contact_name=?, email=?, phone=?, address=? WHERE id=?";

        int rowsAffected = jdbcTemplate.update(updateSql,
                supplierDto.getName(),
                supplierDto.getContactName(),
                supplierDto.getEmail(),
                supplierDto.getPhone(),
                supplierDto.getAddress(),
                supplierDto.getId()
        );
        return supplierDto;
    }



    @Override
    public boolean deleteSupplier(Integer id) {
        String deleteSql = "DELETE FROM suppliers WHERE id=?";
        return jdbcTemplate.update(deleteSql, id)>0;
    }
}