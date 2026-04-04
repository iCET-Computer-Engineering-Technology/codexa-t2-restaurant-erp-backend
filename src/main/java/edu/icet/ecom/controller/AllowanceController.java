package edu.icet.ecom.controller;


import edu.icet.ecom.entity.Allowance;
import edu.icet.ecom.service.AllowanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/allowance")
@CrossOrigin
@RequiredArgsConstructor
public class AllowanceController {
    private final AllowanceService service;
    @GetMapping("/get-all")
    public List<Allowance> getAll(){
        return service.getAllowance();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody Allowance allowance){
        return service.addAllowance(allowance);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody Allowance allowance){
        return service.updateAllowance(allowance);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deleteAllowance(id);
    }

    @GetMapping("/get-allowance/{type}")
    public List<Allowance> getByAllowanceType(@PathVariable String type) {
        return service.getAllowanceByType(type);
    }
}
