package edu.icet.ecom.repository;

import edu.icet.ecom.dto.RevenueResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface RevenueRepository {
    List<RevenueResponseDto> getRevenueByChannel(LocalDate date);
}