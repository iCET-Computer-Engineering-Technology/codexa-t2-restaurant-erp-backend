package edu.icet.ecom.controller;

import edu.icet.ecom.dto.RevenueResponseDto;
import edu.icet.ecom.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/revenue")
@CrossOrigin
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping("/daily-split")
    public List<RevenueResponseDto> getDailySplit(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return revenueService.getDailyChannelRevenue(localDate);
    }
}