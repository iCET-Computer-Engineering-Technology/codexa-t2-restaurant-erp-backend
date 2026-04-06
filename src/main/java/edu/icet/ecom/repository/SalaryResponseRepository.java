package edu.icet.ecom.repository;


import edu.icet.ecom.entity.SalaryResponse;

import java.util.List;

public interface SalaryResponseRepository {
    List<SalaryResponse> getSalaryResponse();
    Boolean addSalaryResponse(SalaryResponse salaryResponse);
    Boolean updateSalaryResponse(SalaryResponse salaryResponse);
    void deleteSalaryResponse(Integer id);
    
}
