package edu.icet.ecom.service.impl;


import edu.icet.ecom.entity.Allowance;
import edu.icet.ecom.repository.AllowanceRepository;
import edu.icet.ecom.service.AllowanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AllowanceServiceImpl implements AllowanceService {

    private final AllowanceRepository repository;

    @Override
    public List<Allowance> getAllowance() {
        return repository.getAllowance();
    }

    @Override
    public Boolean addAllowance(Allowance allowance) {
        return repository.addAllowance(allowance);
    }

    @Override
    public Boolean updateAllowance(Allowance allowance) {
        return repository.updateAllowance(allowance);
    }

    @Override
    public void deleteAllowance(Integer id) {
        repository.deleteAllowance(id);
    }
}
