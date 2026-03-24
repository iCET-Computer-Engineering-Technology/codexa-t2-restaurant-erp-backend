package edu.icet.ecom.service.impl;


import edu.icet.ecom.entity.Deduction;
import edu.icet.ecom.repository.DeductionRepository;
import edu.icet.ecom.service.DeductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeductionServiceImpl implements DeductionService {
    private final DeductionRepository repository;
    @Override
    public List<Deduction> getDeduction() {
        return repository.getDeduction();
    }

    @Override
    public Boolean addDeduction(Deduction deduction) {
        return repository.addDeduction(deduction);
    }

    @Override
    public Boolean updateDeduction(Deduction deduction) {
        return repository.updateDeduction(deduction);
    }

    @Override
    public void deleteDeduction(Integer id) {
            repository.deleteDeduction(id);
    }
}
