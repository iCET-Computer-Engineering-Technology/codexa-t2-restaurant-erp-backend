package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.EmailSchedulerConfig;
import edu.icet.ecom.repository.EmailSchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmailSchedulerConfigRepositoryImpl implements EmailSchedulerConfigRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<EmailSchedulerConfig> ROW_MAPPER = (rs, rowNum) -> {
        Time sendTime = rs.getTime("send_time");
        LocalDateTime createdAt = rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime() : null;
        LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null
                ? rs.getTimestamp("updated_at").toLocalDateTime() : null;

        return EmailSchedulerConfig.builder()
                .id(rs.getInt("id"))
                .sendTime(sendTime != null ? sendTime.toLocalTime() : null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    };

    @Override
    public Optional<EmailSchedulerConfig> findLatestConfig() {
        String sql = "SELECT * FROM email_scheduler_config ORDER BY updated_at DESC, id ASC LIMIT 1";
        List<EmailSchedulerConfig> configs = jdbcTemplate.query(sql, ROW_MAPPER);
        return configs.stream().findFirst();
    }

    @Override
    public EmailSchedulerConfig upsertSendTime(LocalTime sendTime) {
        int updated = jdbcTemplate.update("UPDATE email_scheduler_config SET send_time = ? WHERE id = 1", Time.valueOf(sendTime));
        if (updated == 0) {
            jdbcTemplate.update("INSERT INTO email_scheduler_config (id, send_time) VALUES (1, ?)", Time.valueOf(sendTime));
        }

        EmailSchedulerConfig refreshed = findLatestConfig()
                .orElse(EmailSchedulerConfig.builder().id(1).sendTime(sendTime).build());

        log.info("Scheduler send_time set to {}", refreshed.getSendTime());
        return refreshed;
    }
}

