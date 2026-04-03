package edu.icet.ecom.service.impl;


import edu.icet.ecom.entity.Payroll;
import edu.icet.ecom.repository.PayrollRepository;
import edu.icet.ecom.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private final PayrollRepository repository;
    @Override
    public List<Payroll> getPayroll() {
        return repository.getPayroll();
    }

    @Override
    public Boolean addPayroll(Payroll payroll) {
        return repository.addPayroll(payroll);
    }

    @Override
    public Boolean updatePayroll(Payroll payroll) {
        return repository.updatePayroll(payroll);
    }

    @Override
    public void deletePayroll(Integer id) {
            repository.deletePayroll(id);
    }

    @Override
    public Payroll getPayrollByEmployeeId(Integer id) {
        return repository.getPayrollByEmployeeId(id);
    }

    @Override
    public Double calculateBonus(Integer employeeId) {
        return repository.calculateBonus(employeeId);
    }

    @Override
    public Double calculateDonation(Integer employeeId) {
        return repository.calculateDonation(employeeId);
    }

    @Override
    public Double calculateOvertimeAmount(Integer employeeId) {
        return repository.calculateOvertimeAmount(employeeId);
    }

    @Override
    public Double calculateLaveDays(Integer employeeId) {
        return repository.calculateLaveDays(employeeId);
    }

    @Override
    public Map<String, Double> calculateEPF(Integer employeeId) {
        return repository.calculateEPF(employeeId);
    }

    @Override
    public Map<String, Double>  calculateETF(Integer employeeId) {
        return repository.calculateETF(employeeId);
    }

    @Override
    public Double calculateAllowances(Integer employeeId) {
        return repository.calculateAllowances(employeeId);
    }
}
