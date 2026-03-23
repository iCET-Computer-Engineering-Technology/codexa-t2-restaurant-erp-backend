package edu.icet.ecom.repository;

import edu.icet.ecom.dto.TableDto;

import java.util.List;

public interface TableRepository {
    List<TableDto> findAll();
}
