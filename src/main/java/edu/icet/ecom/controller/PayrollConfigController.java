package edu.icet.ecom.controller;


import edu.icet.ecom.entity.PayrollConfig;
import edu.icet.ecom.service.PayrollConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payroll-config")
@CrossOrigin
@RequiredArgsConstructor
public class PayrollConfigController {
    private final PayrollConfigService service;

    @GetMapping("/get-all")
    public List<PayrollConfig> getAll(){
        return service.getPayrollConfig();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody PayrollConfig payrollConfig){
        return service.createPayrollConfig(payrollConfig);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody PayrollConfig payrollConfig){
        return service.updatePayrollConfig(payrollConfig);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deletePayrollConfig(id);
    }
}
