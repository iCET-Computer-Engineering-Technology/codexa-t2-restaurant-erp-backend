package edu.icet.ecom.service;

import edu.icet.ecom.dto.TableStatusDto;

import java.util.List;

public interface TableManagementService {
    List<TableStatusDto> getAllTableStatuses();
    TableStatusDto getTableById(Integer id);
    void updateTableStatus(Integer tableId, String status);
    void updateTableStatusAutomatic(Integer tableId, String status, String eventType);
    void markTableAsOccupied(Integer tableId);
    void markTableAsAvailable(Integer tableId);
    void markTableAsReserved(Integer tableId);
    void markTableAsCleaning(Integer tableId);
}
