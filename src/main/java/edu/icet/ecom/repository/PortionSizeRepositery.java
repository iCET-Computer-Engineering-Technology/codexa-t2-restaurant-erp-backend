package edu.icet.ecom.repository;

import edu.icet.ecom.dto.PortionSizeDto;

import java.util.List;

public interface PortionSizeRepositery {
    boolean addSize(PortionSizeDto portionSizeDto);
    boolean updateSize(PortionSizeDto portionSizeDto);
    boolean deleteById(Integer id);
    PortionSizeDto searchById(Integer id);
    List<PortionSizeDto> getAll();
}
