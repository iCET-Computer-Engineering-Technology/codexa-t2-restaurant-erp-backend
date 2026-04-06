package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.RevenueResponseDto;
import edu.icet.ecom.repository.RevenueRepository;
import edu.icet.ecom.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevenueServiceImpl implements RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    @Override
    public List<RevenueResponseDto> getDailyChannelRevenue(LocalDate date) {
        return revenueRepository.getRevenueByChannel(date);
    }
}