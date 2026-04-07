package edu.icet.ecom.repository;


import edu.icet.ecom.entity.PayrollConfig;

import java.util.List;

public interface PayrollConfigRepository {
    List<PayrollConfig> getPayrollConfig();
    Boolean addPayrollConfig(PayrollConfig payrollConfig);
    Boolean updatePayrollConfig(PayrollConfig payrollConfig);
    void deletePayrollConfig(Integer id);
}
