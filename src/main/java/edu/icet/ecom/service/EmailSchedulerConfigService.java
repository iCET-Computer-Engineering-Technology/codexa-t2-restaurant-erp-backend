package edu.icet.ecom.service;

import edu.icet.ecom.dto.EmailSchedulerConfigDto;
import edu.icet.ecom.entity.EmailSchedulerConfig;
import edu.icet.ecom.repository.EmailSchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSchedulerConfigService {

    private final EmailSchedulerConfigRepository repository;

    @Value("${app.notifications.cron:0 0 8 * * *}")
    private String defaultCron;

    // Cache with TTL (5 seconds): stores the cron expression and timestamp
    private volatile String cachedCronExpression;
    private volatile long cachedCronTimestamp = 0;
    private static final long CACHE_TTL_MILLIS = 5000; // 5 seconds

    public EmailSchedulerConfigDto getCurrentConfig() {
        Optional<EmailSchedulerConfig> configOpt = repository.findLatestConfig();
        if (configOpt.isEmpty()) {
            return EmailSchedulerConfigDto.builder()
                    .sendTime(parseFallbackTime())
                    .build();
        }
        EmailSchedulerConfig config = configOpt.get();
        return EmailSchedulerConfigDto.builder()
                .id(config.getId())
                .sendTime(config.getSendTime())
                .build();
    }

    public EmailSchedulerConfigDto updateSendTime(LocalTime newTime) {
        if (newTime == null) {
            throw new IllegalArgumentException("sendTime is required");
        }
        EmailSchedulerConfig saved = repository.upsertSendTime(newTime);

        invalidateCache();
        log.info("Scheduler send_time updated to {} and cache invalidated", newTime);

        return EmailSchedulerConfigDto.builder()
                .id(saved.getId())
                .sendTime(saved.getSendTime())
                .build();
    }

    public String getConfiguredTimeAsString() {
        return repository.findLatestConfig()
                .map(config -> config.getSendTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")))
                .orElse(parseFallbackTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
    }

    public String getActiveCronExpression() {
        long now = System.currentTimeMillis();

        if (cachedCronExpression != null && (now - cachedCronTimestamp) < CACHE_TTL_MILLIS) {
            log.debug("Using cached cron expression: {}", cachedCronExpression);
            return cachedCronExpression;
        }

        String cronExpression = repository.findLatestConfig()
                .map(EmailSchedulerConfig::getSendTime)
                .map(this::toCronExpression)
                .orElseGet(this::fallbackCron);

        cachedCronExpression = cronExpression;
        cachedCronTimestamp = now;

        log.info("Cron expression refreshed from DB: {}", cronExpression);
        return cronExpression;
    }

    private void invalidateCache() {
        cachedCronExpression = null;
        cachedCronTimestamp = 0;
        log.debug("Cache invalidated");
    }

    private String toCronExpression(LocalTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        String cron = String.format("0 %d %d * * *", minute, hour);
        log.info("Generated cron expression from time {}: {}", time, cron);
        return cron;
    }

    private String fallbackCron() {
        String cron = (defaultCron != null && !defaultCron.trim().isEmpty())
                ? defaultCron.trim()
                : "0 0 8 * * *";
        log.warn("Scheduler time not configured in DB. Using fallback cron: {}", cron);
        return cron;
    }

    private LocalTime parseFallbackTime() {
        try {
            String cron = fallbackCron();
            String[] parts = cron.split(" ");
            if (parts.length >= 3) {
                int hour = Integer.parseInt(parts[2]);
                int minute = Integer.parseInt(parts[1]);
                int second = Integer.parseInt(parts[0]);
                return LocalTime.of(hour, minute, second);
            }
        } catch (Exception e) {
            log.debug("Unable to parse fallback cron into LocalTime, using 08:00:00", e);
        }
        return LocalTime.of(8, 0);
    }
}
