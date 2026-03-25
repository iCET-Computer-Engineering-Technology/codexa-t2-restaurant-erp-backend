package edu.icet.ecom.repository;

import edu.icet.ecom.dto.PortionsDto;

import java.util.List;

public interface PortionsRepository {
    boolean addPortion(PortionsDto portionsDto);
    boolean updatePortion(PortionsDto portionsDto);
    boolean deleteById(Integer id);
    PortionsDto searchById(Integer id);
    List<PortionsDto> getAll();
}
