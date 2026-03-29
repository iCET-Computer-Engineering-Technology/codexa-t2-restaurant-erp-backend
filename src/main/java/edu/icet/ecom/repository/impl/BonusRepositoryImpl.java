package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Bonus;
import edu.icet.ecom.repository.BonusRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@AllArgsConstructor
public class BonusRepositoryImpl implements BonusRepository {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Bonus> getBonus() {
        String sql ="SELECT * FROM bonus";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Bonus(
                rs.getInt(1),
                rs.getInt(2),
                rs.getDouble(3)
        ));
    }

    @Override
    public Boolean addBonus(Bonus bonus) {
        String sql="INSERT INTO bonus (employee_id,amount) VALUES (?,?)";
        return jdbcTemplate.update(sql,
                bonus.getEmployeeId(),
                bonus.getAmount()
        )>0;
    }

    @Override
    public Boolean updateBonus(Bonus bonus) {
        String sql ="UPDATE bonus SET amount = ? WHERE id = ?";
        return jdbcTemplate.update(sql,bonus.getAmount(),bonus.getId())>0;
    }

    @Override
    public void deleteBonus(Integer id) {
        String sql = "DELETE FROM bonus WHERE id=?";
        jdbcTemplate.update(sql,id);
    }

    @Override
    public List<Bonus> searchBonusById(Integer id) {
        String sql = "SELECT * FROM bonus WHERE id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Bonus(
                rs.getInt(1),
                rs.getInt(2),
                rs.getDouble(3)
        ),id);
    }
}
