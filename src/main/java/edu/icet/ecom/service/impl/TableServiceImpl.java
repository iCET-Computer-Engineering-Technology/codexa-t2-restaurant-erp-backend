package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.TableDto;
import edu.icet.ecom.repository.TableRepository;
import edu.icet.ecom.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository repository;

    @Override
    public List<TableDto> getAll() {
        return repository.findAll();
    }
}

