package edu.icet.ecom.controller;


import edu.icet.ecom.entity.SalaryRequest;
import edu.icet.ecom.service.SalaryRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary-request")
@CrossOrigin
@RequiredArgsConstructor
public class SalaryRequestController {
    private final SalaryRequestService service;

    @GetMapping("/get-all")
    public List<SalaryRequest> getAll(){
        return service.getSalaryRequest();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody SalaryRequest salaryRequest){
        return service.addSalaryRequest(salaryRequest);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody SalaryRequest salaryRequest){
        return service.updateSalaryRequest(salaryRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deleteSalaryRequest(id);
    }
}
