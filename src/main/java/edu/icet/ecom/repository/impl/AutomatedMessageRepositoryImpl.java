package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.AutomatedMessage;
import edu.icet.ecom.repository.AutomatedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AutomatedMessageRepositoryImpl implements AutomatedMessageRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<AutomatedMessage> ROW_MAPPER = (rs, rowNum) -> {
        LocalDateTime createdAt = rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime()
                : null;

        return AutomatedMessage.builder()
                .id(rs.getInt("id"))
                .triggerType(AutomatedMessage.TriggerType.fromDbValue(rs.getString("trigger_type")))
                .channel(AutomatedMessage.Channel.fromDbValue(rs.getString("channel")))
                .templateBody(rs.getString("template_body"))
                .offerType(AutomatedMessage.OfferType.fromDbValue(rs.getString("offer_type")))
                .offerValue(rs.getDouble("offer_value") == 0 ? null : rs.getDouble("offer_value"))
                .sendDaysBefore(rs.getInt("send_days_before"))
                .isActive(rs.getBoolean("is_active"))
                .createdAt(createdAt)
                .build();
    };

    @Override
    public List<AutomatedMessage> findActiveMessages() {
        String sql = "SELECT * FROM automated_messages WHERE is_active = 1 ORDER BY trigger_type, channel";
        log.debug("Fetching all active automated messages");
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public List<AutomatedMessage> findInactiveMessages() {
        String sql = "SELECT * FROM automated_messages WHERE is_active = 0 ORDER BY trigger_type, channel";
        log.debug("Fetching all inactive automated messages");
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public List<AutomatedMessage> findActiveMessagesByTriggerType(AutomatedMessage.TriggerType triggerType) {
        String sql = "SELECT * FROM automated_messages WHERE is_active = 1 AND trigger_type = ? ORDER BY channel";
        log.debug("Fetching active messages for trigger type: {}", triggerType);
        return jdbcTemplate.query(sql, ROW_MAPPER, triggerType.name().toLowerCase());
    }

    @Override
    public Optional<AutomatedMessage> findById(Integer id) {
        String sql = "SELECT * FROM automated_messages WHERE id = ?";
        try {
            AutomatedMessage message = jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
            return Optional.ofNullable(message);
        } catch (Exception e) {
            log.debug("Automated message with ID {} not found", id);
            return Optional.empty();
        }
    }

    @Override
    public Integer save(AutomatedMessage message) {
        String sql = "INSERT INTO automated_messages " +
                "(trigger_type, channel, template_body, offer_type, offer_value, send_days_before, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, message.getTriggerType().name().toLowerCase());
            ps.setString(2, message.getChannel().name().toLowerCase());
            ps.setString(3, message.getTemplateBody());
            ps.setString(4, message.getOfferType().name().toLowerCase());
            ps.setDouble(5, message.getOfferValue() != null ? message.getOfferValue() : 0);
            ps.setInt(6, message.getSendDaysBeforeWithDefault());
            ps.setBoolean(7, message.isActiveRule());
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
        log.info("Saved automated message with ID: {}", id);
        return id;
    }

    @Override
    public boolean update(AutomatedMessage message) {
        String sql = "UPDATE automated_messages SET " +
                "template_body = ?, offer_type = ?, offer_value = ?, send_days_before = ?, is_active = ? " +
                "WHERE id = ?";

        int result = jdbcTemplate.update(sql,
                message.getTemplateBody(),
                message.getOfferType().name().toLowerCase(),
                message.getOfferValue() != null ? message.getOfferValue() : 0,
                message.getSendDaysBeforeWithDefault(),
                message.isActiveRule(),
                message.getId()
        );

        if (result > 0) {
            log.info("Updated automated message with ID: {}", message.getId());
        } else {
            log.warn("Failed to update automated message with ID: {}", message.getId());
        }
        return result > 0;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM automated_messages WHERE id = ?";
        int result = jdbcTemplate.update(sql, id);
        if (result > 0) {
            log.info("Deleted automated message with ID: {}", id);
        }
        return result > 0;
    }
}


