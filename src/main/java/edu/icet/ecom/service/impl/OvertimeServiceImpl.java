package edu.icet.ecom.service.impl;


import edu.icet.ecom.entity.Overtime;
import edu.icet.ecom.repository.OvertimeRepository;
import edu.icet.ecom.service.OvertimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OvertimeServiceImpl implements OvertimeService {
    private final OvertimeRepository repository;
    public List<Overtime> getOvertime() {
        return repository.getOvertime();
    }

    @Override
    public Boolean addOvertime(Overtime overtime) {
        return repository.addOvertime(overtime);
    }

    @Override
    public Boolean updateOvertime(Overtime overtime) {
        return repository.updateOvertime(overtime);
    }

    @Override
    public void deleteOvertime(Integer id) {
            repository.deleteOvertime(id);
    }
}
