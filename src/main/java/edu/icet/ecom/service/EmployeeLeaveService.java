package edu.icet.ecom.service;


import edu.icet.ecom.entity.EmployeeLeave;

import java.util.List;

public interface EmployeeLeaveService {
    List<EmployeeLeave> getEmployeeLeave();
    Boolean addEmployeeLeave(EmployeeLeave employeeLeave);
    Boolean updateEmployeeLeave(EmployeeLeave employeeLeave);
    void deleteEmployeeLeave(Integer id);
}
