package edu.icet.ecom.repository.impl;


import edu.icet.ecom.entity.Payroll;
import edu.icet.ecom.repository.PayrollRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class PayrollRepositoryImpl implements PayrollRepository {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<Payroll> getPayroll() {
        String sql ="SELECT * FROM payroll";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Payroll(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getDouble(3),
                    rs.getDouble(4),
                    rs.getDouble(5),
                    rs.getDouble(6),
                    rs.getDouble(7),
                    rs.getDouble(8),
                    rs.getDouble(9),
                    rs.getDouble(10),
                    rs.getInt(11),
                    rs.getDouble(12),
                    rs.getDouble(13),
                    rs.getDouble(14),
                    rs.getDouble(15),
                    rs.getDate(16).toLocalDate()
        ));
    }

    @Override
    public Boolean addPayroll(Payroll payroll) {
        String sql = """
                INSERT INTO payroll
                (employee_id,basic_salary,epf_employee,
                 epf_employer,etf_employer,allowance,bonus,
                 donation,overtime_amount,leave_days,leave_deduction,
                 total_deduction,net_salary,employer_cost,payroll_date)
                VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;
        return jdbcTemplate.update(sql,
                    payroll.getEmployeeId(),
                    payroll.getBasicSalary(),
                    payroll.getEpfEmployee(),
                    payroll.getEpfEmployer(),
                    payroll.getEtfEmployer(),
                    payroll.getAllowance(),
                    payroll.getBonus(),
                    payroll.getDonation(),
                    payroll.getOvertimeAmount(),
                    payroll.getLeaveDays(),
                    payroll.getLeaveDeduction(),
                    payroll.getTotalDeduction(),
                    payroll.getNetSalary(),
                    payroll.getEmployerCost(),
                    payroll.getPayrollDate()
                )>0;
    }

    @Override
    public Boolean updatePayroll(Payroll payroll) {
        String sql = """
                        UPDATE payroll
                                        SET
                                            employee_id = ?,
                                            basic_salary = ?,
                                            epf_employee = ?,
                                            epf_employer = ?,
                                            etf_employer = ?,
                                            allowance = ?,
                                            bonus = ?,
                                            donation = ?,
                                            overtime_amount = ?,
                                            leave_days = ?,
                                            leave_deduction = ?,
                                            total_deduction = ?,
                                            net_salary = ?,
                                            employer_cost = ?,
                                            payroll_date = ?
                                        WHERE id = ?
                        """;
        return jdbcTemplate.update(sql,
                payroll.getEmployeeId(),
                payroll.getBasicSalary(),
                payroll.getEpfEmployee(),
                payroll.getEpfEmployer(),
                payroll.getEtfEmployer(),
                payroll.getAllowance(),
                payroll.getBonus(),
                payroll.getDonation(),
                payroll.getOvertimeAmount(),
                payroll.getLeaveDays(),
                payroll.getLeaveDeduction(),
                payroll.getTotalDeduction(),
                payroll.getNetSalary(),
                payroll.getEmployerCost(),
                payroll.getPayrollDate(),
                payroll.getId()
                )>0;
    }

    @Override
    public void deletePayroll(Integer id) {
        String sql = "DELETE FROM payroll WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }

    @Override
    public Double calculateBonus(Integer employeeId) {
        String sql = "SELECT SUM(amount) FROM bonus WHERE employee_id = ?";
        Double totalBonus = jdbcTemplate.queryForObject(sql, Double.class, employeeId);
        if (totalBonus != null) {
            return totalBonus;
        }
        return 0.0;
    }

    @Override
    public Double calculateDonation(Integer employeeId) {
        String sql = "SELECT SUM(amount) FROM deduction WHERE employee_id = ?";
        Double totalDeduction = jdbcTemplate.queryForObject(sql, Double.class, employeeId);
        if (totalDeduction != null) {
            return totalDeduction;
        }
        return 0.0;
    }

    @Override
    public Double calculateOvertimeAmount(Integer employeeId) {
        String sql = "SELECT SUM(hours) FROM overtime WHERE employee_id = ?";
        Double totalOvertime = jdbcTemplate.queryForObject(sql, Double.class, employeeId);
        if (totalOvertime != null) {
            return totalOvertime;
        }
        return 0.0;
    }

    @Override
    public Double calculateLaveDays(Integer employeeId) {
        String sql = "SELECT SUM(total_days) FROM employee_leave WHERE employee_id = ?";
        Double totalLeaveDays = jdbcTemplate.queryForObject(sql, Double.class, employeeId);
        if (totalLeaveDays != null) {
            return totalLeaveDays;
        }
        return 0.0;
    }

    @Override
    public Map<String, Double> calculateEPF(Integer employeeId) {
        String salarySql = "SELECT basic_salary FROM employee WHERE id = ?";
        String configSql = "SELECT epf_employee_rate, epf_employer_rate FROM payroll_config LIMIT 1";

        Double basicSalary = jdbcTemplate.queryForObject(salarySql, Double.class, employeeId);

        Map<String, Object> config = jdbcTemplate.queryForMap(configSql);

        Double employeeRate = ((Number) config.get("epf_employee_rate")).doubleValue();
        Double employerRate = ((Number) config.get("epf_employer_rate")).doubleValue();

        // Convert % → decimal
        Double employeeEPF = basicSalary * (employeeRate / 100);
        Double employerEPF = basicSalary * (employerRate / 100);

        return Map.of(
                "employeeEPF", employeeEPF,
                "employerEPF", employerEPF
        );
    }

    @Override
    public Map<String, Double> calculateETF(Integer employeeId) {

        String salarySql = "SELECT basic_salary FROM employee WHERE id = ?";
        String configSql = "SELECT etf_rate FROM payroll_config LIMIT 1";

        Double basicSalary = jdbcTemplate.query(salarySql, rs ->
                rs.next() ? rs.getDouble(1) : 0.0, employeeId
        );

        Double etfRate = jdbcTemplate.query(configSql, rs ->
                rs.next() ? rs.getDouble(1) : 0.0
        );

        Double etf = basicSalary * (etfRate / 100);

        return Map.of(
                "employer", etf
        );
    }
    @Override
    public Double calculateAllowances(Integer employeeId) {
        String sql = "SELECT SUM(amount) FROM allowance WHERE employee_id = ?";

        Double totalAllowance = jdbcTemplate.queryForObject(sql, Double.class, employeeId);

        return totalAllowance != null ? totalAllowance : 0.0;
    }

    @Override
    public Payroll getPayrollByEmployeeId(Integer id) {

        String sql = "SELECT * FROM payroll WHERE employee_id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        new Payroll(
                                rs.getInt("id"),
                                rs.getInt("employee_id"),
                                rs.getDouble("basic_salary"),
                                rs.getDouble("epf_employee"),
                                rs.getDouble("epf_employer"),
                                rs.getDouble("etf_employer"),
                                rs.getDouble("allowance"),
                                rs.getDouble("bonus"),
                                rs.getDouble("donation"),
                                rs.getDouble("overtime_amount"),
                                rs.getInt("leave_days"),
                                rs.getDouble("leave_deduction"),
                                rs.getDouble("total_deduction"),
                                rs.getDouble("net_salary"),
                                rs.getDouble("employer_cost"),
                                rs.getDate("payroll_date").toLocalDate()
                        ),
                id
        );
    }
}
