package edu.icet.ecom.service;


import edu.icet.ecom.entity.Deduction;

import java.util.List;

public interface DeductionService {
    List<Deduction> getDeduction();
    Boolean addDeduction(Deduction deduction);
    Boolean updateDeduction(Deduction deduction);
    void deleteDeduction(Integer id);
}
