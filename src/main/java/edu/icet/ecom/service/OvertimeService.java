package edu.icet.ecom.service;


import edu.icet.ecom.entity.Overtime;

import java.util.List;

public interface OvertimeService {
    List<Overtime> getOvertime();
    Boolean addOvertime(Overtime overtime);
    Boolean updateOvertime(Overtime overtime);
    void deleteOvertime(Integer id);
}
