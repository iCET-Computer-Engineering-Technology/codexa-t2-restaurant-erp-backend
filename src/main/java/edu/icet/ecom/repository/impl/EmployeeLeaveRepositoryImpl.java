package edu.icet.ecom.repository.impl;


import edu.icet.ecom.entity.EmployeeLeave;
import edu.icet.ecom.repository.EmployeeLeaveRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class EmployeeLeaveRepositoryImpl implements EmployeeLeaveRepository {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<EmployeeLeave> getEmployeeLeave() {
        String sql ="SELECT * FROM employee_leave";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new EmployeeLeave(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getDate(4).toLocalDate(),
                        rs.getDate(5).toLocalDate(),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8)
                )
        );
    }

    @Override
    public Boolean addEmployeeLeave(EmployeeLeave employeeLeave) {
        String sql = """
                        INSERT INTO employee_leave
                            (employee_id,leave_type,start_date,end_date, total_days,status,reason)
                        VALUES(?,?,?,?,?,?,?)""";
        return jdbcTemplate.update(sql,
                    employeeLeave.getEmployeeId(),
                    employeeLeave.getLeaveType(),
                    employeeLeave.getStartDate(),
                    employeeLeave.getEndDate(),
                    employeeLeave.getTotalDays(),
                    employeeLeave.getStatus(),
                    employeeLeave.getReason()
                )>0;
    }

    @Override
    public Boolean updateEmployeeLeave(EmployeeLeave employeeLeave) {
        String sql="UPDATE employee_leave set employee_id=?, leave_type = ?, start_date = ?, end_date = ?, total_days = ?, status = ?, reason = ? WHERE id = ?";

        return jdbcTemplate.update(sql,
                    employeeLeave.getEmployeeId(),
                    employeeLeave.getLeaveType(),
                    employeeLeave.getStartDate(),
                    employeeLeave.getEndDate(),
                    employeeLeave.getTotalDays(),
                    employeeLeave.getStatus(),
                    employeeLeave.getReason(),
                    employeeLeave.getId()
                )>0;
    }

    @Override
    public void deleteEmployeeLeave(Integer id) {
        String sql = "DELETE FROM employee_leave WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
