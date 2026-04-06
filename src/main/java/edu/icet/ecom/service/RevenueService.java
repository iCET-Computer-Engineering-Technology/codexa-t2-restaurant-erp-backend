package edu.icet.ecom.service;

import edu.icet.ecom.dto.RevenueResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface RevenueService {
    List<RevenueResponseDto> getDailyChannelRevenue(LocalDate date);
}
