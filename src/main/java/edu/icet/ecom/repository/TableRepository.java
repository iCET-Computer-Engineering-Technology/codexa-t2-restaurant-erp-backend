package edu.icet.ecom.repository;

import edu.icet.ecom.dto.TableDto;
import edu.icet.ecom.dto.TablePositionDto;
import edu.icet.ecom.entity.Table;

import java.util.List;
import java.util.Optional;

public interface TableRepository {
    List<TableDto> findAll();
    Optional<TableDto> findById(Integer id);
    void updateStatus(Integer tableId, String status);
    boolean existsById(Integer id);
    List<TablePositionDto> findAllWithPositions();
    List<TablePositionDto> findBySectionId(Integer sectionId);
    void updateTablePosition(Integer tableId, Integer sectionId, Integer posX, Integer posY);
}
