package edu.icet.ecom.repository;


import edu.icet.ecom.entity.Allowance;

import java.util.List;

public interface AllowanceRepository {
    List<Allowance> getAllowance();
    Boolean addAllowance(Allowance allowance);
    Boolean updateAllowance(Allowance allowance);
    void deleteAllowance(Integer id);

    List<Allowance> getAllowanceByType(String type);
}
