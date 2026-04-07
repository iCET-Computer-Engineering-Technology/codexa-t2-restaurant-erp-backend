package edu.icet.ecom.repository;

import edu.icet.ecom.dto.TableDto;

import java.util.List;
import java.util.Optional;

public interface TableRepository {
    List<TableDto> findAll();
    Optional<TableDto> findById(Integer id);
    void updateStatus(Integer tableId, String status);
    boolean existsById(Integer id);
}
