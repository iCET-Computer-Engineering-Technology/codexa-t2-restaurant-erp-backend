package edu.icet.ecom.service;


import edu.icet.ecom.entity.SalaryRequest;

import java.util.List;

public interface SalaryRequestService {
    List<SalaryRequest> getSalaryRequest();
    Boolean addSalaryRequest(SalaryRequest salaryRequest);
    Boolean updateSalaryRequest(SalaryRequest salaryRequest);
    void deleteSalaryRequest(Integer id);
}
