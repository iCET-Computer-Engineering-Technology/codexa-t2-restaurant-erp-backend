package edu.icet.ecom.service;


import edu.icet.ecom.entity.PayrollConfig;

import java.util.List;

public interface PayrollConfigService {
    List<PayrollConfig> getPayrollConfig();
    Boolean createPayrollConfig(PayrollConfig payrollConfig);
    Boolean updatePayrollConfig(PayrollConfig payrollConfig);
    void deletePayrollConfig(Integer id);
}
