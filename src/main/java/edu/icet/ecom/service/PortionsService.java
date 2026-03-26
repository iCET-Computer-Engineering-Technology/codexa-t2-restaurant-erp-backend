package edu.icet.ecom.service;

import edu.icet.ecom.dto.PortionsDto;

import java.util.List;

public interface PortionsService {
    boolean addPortion(PortionsDto portionsDto);
    boolean updatePortion(PortionsDto portionsDto);
    boolean deleteById(Integer id);
    PortionsDto searchById(Integer id);
    List<PortionsDto> getAll();
}
