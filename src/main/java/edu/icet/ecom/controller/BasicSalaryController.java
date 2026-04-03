package edu.icet.ecom.controller;

import edu.icet.ecom.entity.BasicSalary;
import edu.icet.ecom.service.BasicSalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/basic-salary")
@CrossOrigin
@RequiredArgsConstructor
public class BasicSalaryController {
    private final BasicSalaryService service;
    @GetMapping("/get-all")
    public List<BasicSalary> getAll(){
        return service.getBasicSalary();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody BasicSalary basicSalary){
        return service.addBasicSalary(basicSalary);
    }

    @PutMapping("/update")
    public Boolean updat(@RequestBody BasicSalary basicSalary){
        return service.updateBasicSalary(basicSalary);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deleteBasicSalary(id);
    }

    @GetMapping("/get-by-role/{role}")
    public Map<String, Double> getByRole(@PathVariable String role){
        return service.getBasicSalaryByRole(role);
    }
}
