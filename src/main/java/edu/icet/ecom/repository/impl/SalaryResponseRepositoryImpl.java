package edu.icet.ecom.repository.impl;


import edu.icet.ecom.entity.SalaryResponse;
import edu.icet.ecom.repository.SalaryResponseRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SalaryResponseRepositoryImpl implements SalaryResponseRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<SalaryResponse> getSalaryResponse() {
        String sql = "SELECT * FROM salary_response";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new SalaryResponse(
                        rs.getInt(1),
                        rs.getDouble(2),
                        rs.getDouble(3),
                        rs.getDouble(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getDouble(7),
                        rs.getDouble(8),
                        rs.getDouble(9)
                )
                );
    }

    @Override
    public Boolean addSalaryResponse(SalaryResponse salaryResponse) {
        String sql = """
                        INSERT INTO salary_response
                        (basic_salary,epf_employee,epf_employer,etf_employer,donation,total_deduction,net_salary,employer_cost)
                        (?,?,?,?,?,?,?,?)
                """;
        return jdbcTemplate.update(sql,
                    salaryResponse.getBasicSalary(),
                    salaryResponse.getEpfEmployee(),
                    salaryResponse.getEpfEmployer(),
                    salaryResponse.getEtfEmployer(),
                    salaryResponse.getDonation(),
                    salaryResponse.getTotalDeduction(),
                    salaryResponse.getNetSalary(),
                    salaryResponse.getEmployerCost()
                )>0;

    }

    @Override
    public Boolean updateSalaryResponse(SalaryResponse salaryResponse) {
        String sql = "UPDATE salary_response SET basic_salary = ?, epf_employee = ?, epf_employer = ?, etf_employer = ? donation = ?, total_deduction = ?, net_salary = ?,employer_cost = ? WHERE id = ?";

        return jdbcTemplate.update(sql,
                    salaryResponse.getBasicSalary(),
                    salaryResponse.getEpfEmployee(),
                    salaryResponse.getEpfEmployer(),
                    salaryResponse.getEtfEmployer(),
                    salaryResponse.getDonation(),
                    salaryResponse.getTotalDeduction(),
                    salaryResponse.getNetSalary(),
                    salaryResponse.getEmployerCost(),
                    salaryResponse.getId()
                )>0;
    }

    @Override
    public void deleteSalaryResponse(Integer id) {
            String sql = "DELETE FROM salary_response WHERE id =  ?";
            jdbcTemplate.update(sql,id);
    }
}
