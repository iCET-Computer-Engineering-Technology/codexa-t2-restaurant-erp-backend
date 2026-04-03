package edu.icet.ecom.repository;


import edu.icet.ecom.entity.SalaryRequest;

import java.util.List;

public interface SalaryRequestRepository {
    List<SalaryRequest> getSalaryRequest();
    Boolean addSalaryRequest(SalaryRequest salaryRequest);
    Boolean updateSalaryRequest(SalaryRequest salaryRequest);
    void deleteSalaryRequest(Integer id);
}
