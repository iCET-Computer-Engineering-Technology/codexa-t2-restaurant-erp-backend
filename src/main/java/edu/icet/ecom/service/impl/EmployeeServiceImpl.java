package edu.icet.ecom.service.impl;


import edu.icet.ecom.entity.Employee;
import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.repository.EmployeeRepository;
import edu.icet.ecom.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    //user
    public List<UserEntity> getAllUsers() {
        return repository.getAllUser();
    }

    //employee
    @Override
    public List<Employee> getEmployee() {
        return repository.getEmployee();
    }

    public Boolean addEmployee(Employee employee) {
        return repository.addEmployee(employee);
    }

    @Override
    public Boolean updateEmployee(Employee employee) {
        return repository.updateEmployee(employee);
    }

    @Override
    public void deleteEmployee(Integer id) {
        repository.deleteEmployee(id);
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        return repository.getEmployeeById(id);
    }

    @Override
    public Boolean updateEmployeeById(Integer id,Employee employee) {
        return repository.updateEmployeeById(id,employee);
    }


}
