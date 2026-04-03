package edu.icet.ecom.repository.impl;


import edu.icet.ecom.entity.SalaryRequest;
import edu.icet.ecom.repository.SalaryRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SalaryRequestRepositoryImpl implements SalaryRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<SalaryRequest> getSalaryRequest() {
        String sql="SELECT * FROM salary_request";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new SalaryRequest(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDouble(3),
                        rs.getDouble(4)
                )

        );
    }

    @Override
    public Boolean addSalaryRequest(SalaryRequest salaryRequest) {
        String sql = """
                        INSERT INTO salary_request
                        (employee_id,basic_salary,donation)
                        VALUES(?,?,?)
                        """;
        return jdbcTemplate.update(sql,
                    salaryRequest.getEmployeeId(),
                    salaryRequest.getBasicSalary(),
                    salaryRequest.getDonation()
                )>0;

    }

    @Override
    public Boolean updateSalaryRequest(SalaryRequest salaryRequest) {
        String sql ="UPDATE salary_request SET employeeId = ?,basicSalary = ?, donation = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                    salaryRequest.getEmployeeId(),
                    salaryRequest.getBasicSalary(),
                    salaryRequest.getDonation()
                )>0;
    }

    @Override
    public void deleteSalaryRequest(Integer id) {
        String sql ="DELETE FROM salary_request WHERE id = ?";
        jdbcTemplate.update(sql,id);
    }
}
