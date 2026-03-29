package edu.icet.ecom.service;


import edu.icet.ecom.entity.Allowance;

import java.util.List;

public interface AllowanceService {
    List<Allowance> getAllowance();
    Boolean addAllowance(Allowance allowance);
    Boolean updateAllowance(Allowance allowance);
    void deleteAllowance(Integer id);

    List<Allowance> getAllowanceByType(String type);
}
