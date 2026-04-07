package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.TableDto;
import edu.icet.ecom.dto.TableStatusDto;
import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.repository.TableRepository;
import edu.icet.ecom.service.TableManagementService;
import edu.icet.ecom.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableManagementServiceImpl implements TableManagementService {

    private final TableRepository tableRepository;
    private final WebSocketNotificationService webSocketNotificationService;

    private static final Set<String> VALID_STATUSES = Set.of("available", "occupied", "reserved", "cleaning");

    @Override
    public List<TableStatusDto> getAllTableStatuses() {
        log.info("Fetching all table statuses");
        return tableRepository.findAll().stream()
                .map(this::convertToStatusDto)
                .collect(Collectors.toList());
    }

    @Override
    public TableStatusDto getTableById(Integer id) {
        log.info("Fetching table status for table ID: {}", id);
        return tableRepository.findById(id)
                .map(this::convertToStatusDto)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with ID: " + id));
    }

    @Override
    public void updateTableStatus(Integer tableId, String status) {
        log.info("Manually updating table {} to status: {}", tableId, status);
        validateStatus(status);
        validateTableExists(tableId);
        
        tableRepository.updateStatus(tableId, status);
        
        // Broadcast update via WebSocket
        TableDto updatedTable = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with ID: " + tableId));
        
        broadcastTableUpdate(updatedTable, "MANUAL_UPDATE");
    }

    @Override
    public void updateTableStatusAutomatic(Integer tableId, String status, String eventType) {
        log.info("Automatically updating table {} to status: {} due to event: {}", tableId, status, eventType);
        
        try {
            validateStatus(status);
            
            if (!tableRepository.existsById(tableId)) {
                log.warn("Table {} does not exist, skipping automatic status update", tableId);
                return;
            }
            
            tableRepository.updateStatus(tableId, status);
            
            // Broadcast update via WebSocket
            TableDto updatedTable = tableRepository.findById(tableId).orElse(null);
            if (updatedTable != null) {
                broadcastTableUpdate(updatedTable, eventType);
            }
        } catch (Exception e) {
            log.error("Error automatically updating table status for table {}: {}", tableId, e.getMessage());
            // Non-blocking: don't throw exception for automatic updates
        }
    }

    @Override
    public void markTableAsOccupied(Integer tableId) {
        updateTableStatus(tableId, "occupied");
    }

    @Override
    public void markTableAsAvailable(Integer tableId) {
        updateTableStatus(tableId, "available");
    }

    @Override
    public void markTableAsReserved(Integer tableId) {
        updateTableStatus(tableId, "reserved");
    }

    @Override
    public void markTableAsCleaning(Integer tableId) {
        updateTableStatus(tableId, "cleaning");
    }

    private void validateStatus(String status) {
        if (status == null || !VALID_STATUSES.contains(status.toLowerCase())) {
            throw new IllegalArgumentException("Invalid status. Must be one of: " + VALID_STATUSES);
        }
    }

    private void validateTableExists(Integer tableId) {
        if (!tableRepository.existsById(tableId)) {
            throw new ResourceNotFoundException("Table not found with ID: " + tableId);
        }
    }

    private void broadcastTableUpdate(TableDto table, String eventType) {
        try {
            webSocketNotificationService.notifyTableStatusUpdate(
                    table.getId(),
                    table.getTableNumber(),
                    table.getCapacity(),
                    table.getStatus(),
                    eventType
            );
            log.info("Broadcasted table status update for table {} via WebSocket", table.getId());
        } catch (Exception e) {
            log.error("Failed to broadcast table status update: {}", e.getMessage());
            // Non-blocking: don't throw exception if WebSocket broadcast fails
        }
    }

    private TableStatusDto convertToStatusDto(TableDto tableDto) {
        return TableStatusDto.builder()
                .id(tableDto.getId())
                .tableNumber(tableDto.getTableNumber())
                .capacity(tableDto.getCapacity())
                .status(tableDto.getStatus())
                .build();
    }
}
