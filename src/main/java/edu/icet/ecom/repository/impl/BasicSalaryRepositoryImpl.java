package edu.icet.ecom.repository.impl;


import edu.icet.ecom.entity.BasicSalary;
import edu.icet.ecom.repository.BasicSalaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class BasicSalaryRepositoryImpl implements BasicSalaryRepository {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<BasicSalary> getBasicSalary() {
        String sql ="SELECT * FROM basic_salary";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new BasicSalary(
                rs.getInt(1),
                rs.getString(2),
                rs.getDouble(3)));
    }

    @Override
    public Map<String, Double> getBasicSalaryByRole(String role) {
        String sql = "SELECT amount FROM basic_salary WHERE role = ?";

        Double amount = jdbcTemplate.queryForObject(sql, Double.class, role);

        return Map.of("amount", amount);
    }

    @Override
    public Boolean addBasicSalary(BasicSalary basicSalary) {
        String sql ="INSERT INTO basic_salary (role,amount) VALUES (?,?)";
        return   jdbcTemplate.update(sql,
                basicSalary.getRoleName(),
                basicSalary.getAmount()
                )>0;
    }

    @Override
    public Boolean updateBasicSalary(BasicSalary basicSalary) {
        String sql ="UPDATE basic_salary SET role = ?, amount = ? WHERE id = ?";
        return  jdbcTemplate.update(sql,
                    basicSalary.getRoleName(),
                    basicSalary.getAmount(),
                    basicSalary.getId()
                )>0;
    }

    @Override
    public void deleteBasicSalary(Integer id) {
        String sql ="DELETE FROM basic_salary WHERE id = ?";
        jdbcTemplate.update(sql,id);
    }
}
