package edu.icet.ecom.controller;

import edu.icet.ecom.dto.PortionsDto;
import edu.icet.ecom.service.PortionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/portions", "/api/portions"})
@RequiredArgsConstructor
public class PortionsController {

    private final PortionsService portionsService;

    @PostMapping
    public boolean addPortion(@RequestBody PortionsDto portionsDto) {
        return portionsService.addPortion(portionsDto);
    }

    @PutMapping
    public boolean updatePortion(@RequestBody PortionsDto portionsDto) {
        return portionsService.updatePortion(portionsDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        return portionsService.deleteById(id);
    }

    @GetMapping("/{id}")
    public PortionsDto searchById(@PathVariable Integer id) {
        return portionsService.searchById(id);
    }

    @GetMapping
    public List<PortionsDto> getAll() {
        return portionsService.getAll();
    }
}

