package edu.icet.ecom.controller;

import edu.icet.ecom.dto.EmailSchedulerConfigDto;
import edu.icet.ecom.service.EmailSchedulerConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/email-scheduler")
@RequiredArgsConstructor
@Slf4j
public class EmailSchedulerConfigController {

    private final EmailSchedulerConfigService schedulerConfigService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<EmailSchedulerConfigDto> getSchedulerConfig() {
        return ResponseEntity.ok(schedulerConfigService.getCurrentConfig());
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<EmailSchedulerConfigDto> updateSchedulerConfig(
            @Valid @RequestBody EmailSchedulerConfigDto request) {
        if (request.getSendTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        log.info("Updating email scheduler time to {}", request.getSendTime());
        return ResponseEntity.ok(schedulerConfigService.updateSendTime(request.getSendTime()));
    }
}

