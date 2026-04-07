package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Deduction;
import edu.icet.ecom.repository.DeductionRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class DeductionRepositoryImpl implements DeductionRepository {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Deduction> getDeduction() {
        String sql ="SELECT * FROM deduction";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Deduction(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getDouble(4)
        ));
    }

    @Override
    public Boolean addDeduction(Deduction deduction) {
        String sql = "INSERT INTO deduction (employee_id,type,amount)VALUES(?,?,?)";
        return jdbcTemplate.update(sql,
                    deduction.getEmployeeId(),
                    deduction.getType(),
                    deduction.getAmount()
                )>0;
    }

    @Override
    public Boolean updateDeduction(Deduction deduction) {
        String sql = "UPDATE deduction SET amount = ? WHERE id = ?";
        return jdbcTemplate.update(sql,deduction.getAmount(),deduction.getId())>0;
    }

    @Override
    public void deleteDeduction(Integer id) {
            String sql = "DELETE FROM deduction WHERE id=?";
            jdbcTemplate.update(sql,id);
    }
}
