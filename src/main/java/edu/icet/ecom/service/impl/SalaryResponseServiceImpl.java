package edu.icet.ecom.service.impl;


import edu.icet.ecom.entity.SalaryResponse;
import edu.icet.ecom.repository.SalaryResponseRepository;
import edu.icet.ecom.service.SalaryResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryResponseServiceImpl implements SalaryResponseService {
    private final SalaryResponseRepository repository;
    @Override
    public List<SalaryResponse> getSalaryResponse() {
        return repository.getSalaryResponse();
    }

    @Override
    public Boolean addSalaryResponse(SalaryResponse salaryResponse) {
        return repository.addSalaryResponse(salaryResponse);
    }

    @Override
    public Boolean updateSalaryResponse(SalaryResponse salaryResponse) {
        return repository.updateSalaryResponse(salaryResponse);
    }

    @Override
    public void deleteSalaryResponse(Integer id) {
            repository.deleteSalaryResponse(id);
    }
}
