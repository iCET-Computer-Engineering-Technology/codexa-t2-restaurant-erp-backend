package edu.icet.ecom.controller;


import edu.icet.ecom.entity.EmployeeLeave;
import edu.icet.ecom.service.EmployeeLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee-leave")
@CrossOrigin
@RequiredArgsConstructor
public class EmployeeLeaveController {
    private final EmployeeLeaveService service;

    @GetMapping("/get-all")
    public List<EmployeeLeave> getAll(){
        return service.getEmployeeLeave();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody EmployeeLeave employeeleave){
        return service.addEmployeeLeave(employeeleave);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody EmployeeLeave employeeleave){
        return service.updateEmployeeLeave(employeeleave);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deleteEmployeeLeave(id);
    }
}
