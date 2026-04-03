package edu.icet.ecom.controller;


import edu.icet.ecom.entity.Overtime;
import edu.icet.ecom.service.OvertimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/overtime")
@CrossOrigin
@RequiredArgsConstructor
public class OvertimeController {
    private final OvertimeService service;

    @GetMapping("/get-all")
    public List<Overtime> getAll(){
        return service.getOvertime();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody Overtime overtime){
        return service.addOvertime(overtime);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody Overtime overtime){
        return service.updateOvertime(overtime);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deleteOvertime(id);
    }
}
