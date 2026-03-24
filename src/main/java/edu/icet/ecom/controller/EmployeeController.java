package edu.icet.ecom.controller;


import edu.icet.ecom.entity.Employee;
import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@CrossOrigin
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;

    @GetMapping("/get-all-user")
    public List<UserEntity> getAllUser(){
        return service.getAllUsers();
    }

    @GetMapping("/get-all")
    public List<Employee> getAll(){
        return service.getEmployee();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody Employee employee){
        return service.addEmployee(employee);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody Employee employee){
        return service.updateEmployee(employee);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
            service.deleteEmployee(id);
    }

    @GetMapping("/getById/{id}")
    public Employee getEmployeeById(@PathVariable Integer id){
        return service.getEmployeeById(id);
    }
    @PutMapping("/update/{id}")
    public Boolean updateEmployee(@PathVariable Integer id, @RequestBody Employee employee){
        return service.updateEmployeeById(id, employee);
    }
}
