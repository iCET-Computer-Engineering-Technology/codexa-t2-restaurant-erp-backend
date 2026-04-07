package edu.icet.ecom.service;


import edu.icet.ecom.entity.Employee;
import edu.icet.ecom.entity.UserEntity;

import java.util.List;

public interface EmployeeService {
    List<UserEntity> getAllUsers();
    List<Employee> getEmployee();
    Boolean addEmployee(Employee employee);
    Boolean updateEmployee(Employee employee);
    void deleteEmployee(Integer id);
    Employee getEmployeeById(Integer id);
    Boolean updateEmployeeById(Integer id,Employee employee);
}
