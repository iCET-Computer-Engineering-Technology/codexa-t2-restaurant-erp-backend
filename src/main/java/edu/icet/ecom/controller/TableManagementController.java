package edu.icet.ecom.controller;

import edu.icet.ecom.dto.TableStatusDto;
import edu.icet.ecom.dto.TableUpdateRequest;
import edu.icet.ecom.service.TableManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({ "/api/table-management"})
@RequiredArgsConstructor
@CrossOrigin
public class TableManagementController {

    private final TableManagementService tableManagementService;

    @GetMapping
    public ResponseEntity<List<TableStatusDto>> getAllTableStatuses() {
        List<TableStatusDto> tables = tableManagementService.getAllTableStatuses();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableStatusDto> getTableById(@PathVariable Integer id) {
        TableStatusDto table = tableManagementService.getTableById(id);
        return ResponseEntity.ok(table);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TableStatusDto> updateTableStatus(
            @PathVariable Integer id,
            @Valid @RequestBody TableUpdateRequest request) {
        tableManagementService.updateTableStatus(id, request.getStatus());
        TableStatusDto updatedTable = tableManagementService.getTableById(id);
        return ResponseEntity.ok(updatedTable);
    }

    @PutMapping("/{id}/occupy")
    public ResponseEntity<TableStatusDto> occupyTable(@PathVariable Integer id) {
        tableManagementService.markTableAsOccupied(id);
        TableStatusDto updatedTable = tableManagementService.getTableById(id);
        return ResponseEntity.ok(updatedTable);
    }

    @PutMapping("/{id}/free")
    public ResponseEntity<TableStatusDto> freeTable(@PathVariable Integer id) {
        tableManagementService.markTableAsAvailable(id);
        TableStatusDto updatedTable = tableManagementService.getTableById(id);
        return ResponseEntity.ok(updatedTable);
    }

    @PutMapping("/{id}/reserve")
    public ResponseEntity<TableStatusDto> reserveTable(@PathVariable Integer id) {
        tableManagementService.markTableAsReserved(id);
        TableStatusDto updatedTable = tableManagementService.getTableById(id);
        return ResponseEntity.ok(updatedTable);
    }

    @PutMapping("/{id}/cleaning")
    public ResponseEntity<TableStatusDto> markTableCleaning(@PathVariable Integer id) {
        tableManagementService.markTableAsCleaning(id);
        TableStatusDto updatedTable = tableManagementService.getTableById(id);
        return ResponseEntity.ok(updatedTable);
    }
}
