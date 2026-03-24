package edu.icet.ecom.repository;


import edu.icet.ecom.entity.EmployeeLeave;

import java.util.List;

public interface EmployeeLeaveRepository {
    List<EmployeeLeave> getEmployeeLeave();
    Boolean addEmployeeLeave(EmployeeLeave employeeLeave);
    Boolean updateEmployeeLeave(EmployeeLeave employeeLeave);
    void deleteEmployeeLeave(Integer id);
}
