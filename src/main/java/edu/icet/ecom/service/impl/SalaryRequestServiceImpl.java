package edu.icet.ecom.service.impl;


import edu.icet.ecom.entity.SalaryRequest;
import edu.icet.ecom.repository.SalaryRequestRepository;
import edu.icet.ecom.service.SalaryRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryRequestServiceImpl implements SalaryRequestService {
    private final SalaryRequestRepository repository;
    @Override
    public List<SalaryRequest> getSalaryRequest() {
        return repository.getSalaryRequest();
    }

    @Override
    public Boolean addSalaryRequest(SalaryRequest salaryRequest) {
        return repository.addSalaryRequest(salaryRequest);
    }

    @Override
    public Boolean updateSalaryRequest(SalaryRequest salaryRequest) {
        return repository.updateSalaryRequest(salaryRequest);
    }

    @Override
    public void deleteSalaryRequest(Integer id) {
            repository.deleteSalaryRequest(id);
    }
}
