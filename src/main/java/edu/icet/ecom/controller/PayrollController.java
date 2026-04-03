package edu.icet.ecom.controller;

import edu.icet.ecom.entity.Payroll;
import edu.icet.ecom.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payroll")
@CrossOrigin
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService service;

    @GetMapping("/get-all")
    public List<Payroll> getAll(){
        return service.getPayroll();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody Payroll payroll){
        return service.addPayroll(payroll);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody Payroll payroll){
        return service.updatePayroll(payroll);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deletePayroll(id);
    }

    @GetMapping("/getById/{employeeId}")
    public Payroll getPayrollByEmployeeId(@PathVariable Integer employeeId){
        return service.getPayrollByEmployeeId(employeeId);
    }

    @GetMapping("/totalBonus/{employeeId}")
    public Double getTotalBonus(@PathVariable Integer employeeId) {
        return service.calculateBonus(employeeId);
    }
    @GetMapping("/totalDonation/{employeeId}")
    public Double getTotalDonation(@PathVariable Integer employeeId) {
        return service.calculateDonation(employeeId);
    }
    @GetMapping("/totalOvertime/{employeeId}")
    public Double getTotalOvertime(@PathVariable Integer employeeId) {
        return service.calculateOvertimeAmount(employeeId);
    }
    @GetMapping("/totalLaveDays/{employeeId}")
    public Double getTotalLaveDays(@PathVariable Integer employeeId) {
        return service.calculateLaveDays(employeeId);
    }
    @GetMapping("/totalEPF/{employeeId}")
    public Map<String, Double> getTotalEPF(@PathVariable Integer employeeId) {
        return service.calculateEPF(employeeId);
    }
    @GetMapping("/totalETF/{employeeId}")
    public Map<String, Double>  getTotalETF(@PathVariable Integer employeeId) {
        return service.calculateETF(employeeId);
    }
    @GetMapping("/totalAllowances/{employeeId}")
    public Double getTotalAllowances(@PathVariable Integer employeeId) {
        return service.calculateAllowances(employeeId);
    }


}
