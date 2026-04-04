package edu.icet.ecom.service;


import edu.icet.ecom.entity.Payroll;

import java.util.List;
import java.util.Map;

public interface PayrollService {
    List<Payroll> getPayroll();
    Boolean addPayroll(Payroll payroll);
    Boolean updatePayroll(Payroll payroll);
    void deletePayroll(Integer id);
    Payroll getPayrollByEmployeeId(Integer id);
    Double calculateBonus(Integer employeeId);
    Double calculateDonation(Integer employeeId);
    Double calculateOvertimeAmount(Integer employeeId);
    Double calculateLaveDays(Integer employeeId);
    Map<String, Double> calculateEPF(Integer employeeId);
    Map<String, Double>  calculateETF(Integer employeeId);
    Double calculateAllowances(Integer employeeId);

}
