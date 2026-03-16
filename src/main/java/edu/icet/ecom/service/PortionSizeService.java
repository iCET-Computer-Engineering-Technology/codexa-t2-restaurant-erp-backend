package edu.icet.ecom.service;

import edu.icet.ecom.dto.PortionSizeDto;

import java.util.List;

public interface PortionSizeService {
    boolean addSize(PortionSizeDto portionSizeDto);
    boolean updateSize(PortionSizeDto portionSizeDto);
    boolean deleteById(Integer id);
    PortionSizeDto searchById(Integer id);
    List<PortionSizeDto> getAll();
}
