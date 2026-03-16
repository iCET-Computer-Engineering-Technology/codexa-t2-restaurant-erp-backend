package edu.icet.ecom.controller;

import edu.icet.ecom.dto.PortionSizeDto;
import edu.icet.ecom.service.PortionSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portionSize")
@RequiredArgsConstructor
public class PortionSizeController {

    private final PortionSizeService service;

    @PostMapping
    public boolean addSize(@RequestBody PortionSizeDto portionSizeDto){
        return service.addSize(portionSizeDto);
    }

    @PutMapping
    public boolean updateSize(@RequestBody PortionSizeDto portionSizeDto){
        return  service.updateSize(portionSizeDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable Integer id){
        return service.deleteById(id);
    }

    @GetMapping("/{id}")
    public PortionSizeDto searchById(@PathVariable Integer id){
        return service.searchById(id);
    }

    @GetMapping
    public List<PortionSizeDto> getAll(){
        return service.getAll();
    }
}
