package edu.icet.ecom.controller;

import edu.icet.ecom.dto.PortionSizeDto;
import edu.icet.ecom.service.PortionSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/portion-size")
@RequiredArgsConstructor
public class PortionSizeController {

    private final PortionSizeService service;

    @GetMapping("/getAll")
    public List<PortionSizeDto> getAll(){
        return service.getAll();
    }
}
