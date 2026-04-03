package edu.icet.ecom.repository;


import edu.icet.ecom.entity.Deduction;

import java.util.List;

public interface DeductionRepository {
    List<Deduction> getDeduction();
    Boolean addDeduction(Deduction deduction);
    Boolean updateDeduction(Deduction deduction);
    void deleteDeduction(Integer id);
}
