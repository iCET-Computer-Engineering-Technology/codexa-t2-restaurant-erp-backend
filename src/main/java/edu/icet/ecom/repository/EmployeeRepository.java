package edu.icet.ecom.repository;


import edu.icet.ecom.entity.Employee;
import edu.icet.ecom.entity.UserEntity;

import java.util.List;

public interface EmployeeRepository {
    List<Employee> getEmployee();
    Boolean addEmployee(Employee employee);
    Boolean updateEmployee(Employee employee);
    void deleteEmployee(Integer id);
    List<UserEntity> getAllUser();
    Employee getEmployeeById(Integer id);
    Boolean updateEmployeeById(Integer id,Employee employee);
}
