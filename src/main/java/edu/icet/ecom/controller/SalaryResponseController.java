package edu.icet.ecom.controller;


import edu.icet.ecom.entity.SalaryResponse;
import edu.icet.ecom.service.SalaryResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
        @RequestMapping("/salary-response")
@CrossOrigin
@RequiredArgsConstructor
public class SalaryResponseController {
    private final SalaryResponseService service;

    @GetMapping("/get-all")
    public List<SalaryResponse> getAll(){
        return service.getSalaryResponse();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody SalaryResponse salaryResponse){
        return service.addSalaryResponse(salaryResponse);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody SalaryResponse salaryResponse){
        return service.updateSalaryResponse(salaryResponse);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deleteSalaryResponse(id);
    }
}
