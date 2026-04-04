package edu.icet.ecom.service.impl;


import edu.icet.ecom.entity.PayrollConfig;
import edu.icet.ecom.repository.PayrollConfigRepository;
import edu.icet.ecom.service.PayrollConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollConfigServiceImpl implements PayrollConfigService {

    private final PayrollConfigRepository repository;

    @Override
    public List<PayrollConfig> getPayrollConfig() {
        return repository.getPayrollConfig();
    }

    @Override
    public Boolean createPayrollConfig(PayrollConfig payrollConfig) {
        return repository.addPayrollConfig(payrollConfig);
    }

    @Override
    public Boolean updatePayrollConfig(PayrollConfig payrollConfig) {
        return repository.updatePayrollConfig(payrollConfig);
    }

    @Override
    public void deletePayrollConfig(Integer id) {
        repository.deletePayrollConfig(id);
    }
}
