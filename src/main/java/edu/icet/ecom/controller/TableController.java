package edu.icet.ecom.controller;

import edu.icet.ecom.dto.TableDto;
import edu.icet.ecom.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/tables", "/api/tables"})
@RequiredArgsConstructor
@CrossOrigin
public class TableController {

    private final TableService tableService;

    @GetMapping
    public List<TableDto> getAll() {
        return tableService.getAll();
    }
}

