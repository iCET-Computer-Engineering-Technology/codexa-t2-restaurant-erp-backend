package edu.icet.ecom.repository;


import edu.icet.ecom.entity.BasicSalary;

import java.util.List;
import java.util.Map;

public interface BasicSalaryRepository {
        List<BasicSalary> getBasicSalary();
        Map<String, Double> getBasicSalaryByRole(String role);
        Boolean addBasicSalary(BasicSalary basicSalary);
        Boolean updateBasicSalary(BasicSalary basicSalary);
        void deleteBasicSalary(Integer id);
}
