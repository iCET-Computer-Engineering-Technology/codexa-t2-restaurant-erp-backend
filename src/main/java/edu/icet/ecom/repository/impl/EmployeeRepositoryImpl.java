package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Employee;
import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.repository.EmployeeRepository;
import edu.icet.ecom.util.Role;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    //user
    public List<UserEntity> getAllUser() {
        String sql = "SELECT id, username, email, password, role, enabled, is_online, last_active_at, created_at FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserEntity(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role") != null ? Role.valueOf(rs.getString("role").toUpperCase()) : null,
                rs.getBoolean("enabled"),
                rs.getBoolean("is_online"),
                rs.getTimestamp("last_active_at") != null ? rs.getTimestamp("last_active_at").toLocalDateTime() : null,
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
        ));
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        String sql = "SELECT * FROM employee WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Employee(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getDouble(7),
                rs.getDouble(8),
                rs.getDouble(9),
                rs.getDouble(10),
                rs.getString(11),
                rs.getString(12),
                rs.getString(13)
        ), id);
    }

    @Override
    public Boolean updateEmployeeById(Integer id,Employee employee) {
        String sql = "UPDATE employee SET allowance = ?, bonus = ?, donation = ? WHERE id = ?";

        return jdbcTemplate.update(sql,
                employee.getAllowance(),
                employee.getBonus(),
                employee.getDonation(),
                id) > 0;
    }

    //employee
    @Override
    public List<Employee> getEmployee() {
        String sql = "SELECT * FROM employee";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Employee(
                rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getDouble(7),
                rs.getDouble(8),
                rs.getDouble(9),
                rs.getDouble(10),
                rs.getString(11),
                rs.getString(12),
                rs.getString(13)
        ));
    }

    public Boolean addEmployee(Employee employee) {
        String sql = """
            INSERT INTO employee
            (user_id, first_name, last_name, email, phone, basic_salary, department,designation)
            VALUES (?, ?, ?, ?, ?, ?, ?,?)
        """;

        return jdbcTemplate.update(
                sql,
                employee.getUser_id(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getBasicSalary(),
                employee.getDepartment(),
                employee.getDesignation()
        ) > 0;
    }

    @Override
    public Boolean updateEmployee(Employee employee) {
        String sql ="UPDATE employee SET user_id = ?, first_name = ?, last_name = ?, email = ?, phone = ?,basic_salary=?,department =?, designation= ?  WHERE id = ?";

        return jdbcTemplate.update(
                sql,
                employee.getUser_id(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getBasicSalary(),
                employee.getDepartment(),
                employee.getDesignation(),
                employee.getId()
        ) > 0;
    }



    @Override
    public void deleteEmployee(Integer id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
