package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Chef;
import edu.icet.ecom.repository.ChefRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChefRepositoryImpl implements ChefRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChefRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Chef> findAvailableChefs() {

        String sql = """
            SELECT * FROM chef
            WHERE availability='available'
            ORDER BY current_task_load ASC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Chef chef = new Chef();
            chef.setId(rs.getLong("id"));
            chef.setName(rs.getString("name"));
            chef.setAvailability(rs.getString("availability"));
            chef.setCurrentTaskLoad(rs.getInt("current_task_load"));
            return chef;
        });
    }

    @Override
    public Chef findById(Long id) {
        String sql = "SELECT * FROM chef WHERE id=?";
        return jdbcTemplate.queryForObject(sql,(rs,rowNum)->{
            Chef c = new Chef();
            c.setId(rs.getLong("id"));
            c.setName(rs.getString("name"));
            c.setAvailability(rs.getString("availability"));
            c.setCurrentTaskLoad(rs.getInt("current_task_load"));
            return c;
        },id);
    }

    @Override
    public void increaseTaskLoad(Long chefId) {
        jdbcTemplate.update(
                "UPDATE chef SET current_task_load = current_task_load + 1 WHERE id=?",
                chefId
        );
    }

    @Override
    public void decreaseTaskLoad(Long chefId) {
        jdbcTemplate.update(
                "UPDATE chef SET current_task_load = current_task_load - 1 WHERE id=?",
                chefId
        );
    }
}
