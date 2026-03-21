package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.CampaignAnalytics;
import edu.icet.ecom.repository.CampaignAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CampaignAnalyticsRepositoryImpl implements CampaignAnalyticsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer save(CampaignAnalytics analytics) {
        String sql = "INSERT INTO campaign_analytics (campaign_id, customer_id, sent_at, variant) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, analytics.getCampaignId());
            ps.setInt(2, analytics.getCustomerId());
            ps.setObject(3, analytics.getSentAt());
            ps.setString(4, analytics.getVariant());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : null;
    }

    @Override
    public boolean update(CampaignAnalytics analytics) {
        String sql = "UPDATE campaign_analytics SET opened_at = ?, clicked_at = ?, converted_at = ?, unsubscribed_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                analytics.getOpenedAt(),
                analytics.getClickedAt(),
                analytics.getConvertedAt(),
                analytics.getUnsubscribedAt(),
                analytics.getId()) > 0;
    }

    @Override
    public List<CampaignAnalytics> findByCampaignId(Integer campaignId) {
        String sql = "SELECT id, campaign_id, customer_id, sent_at, opened_at, clicked_at, converted_at, unsubscribed_at, variant " +
                "FROM campaign_analytics WHERE campaign_id = ?";

        return jdbcTemplate.query(sql, new Object[]{campaignId}, (rs, rowNum) -> {
            CampaignAnalytics analytics = new CampaignAnalytics();
            analytics.setId(rs.getInt(1));
            analytics.setCampaignId(rs.getInt(2));
            analytics.setCustomerId(rs.getInt(3));
            analytics.setSentAt(rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null);
            analytics.setOpenedAt(rs.getTimestamp(5) != null ? rs.getTimestamp(5).toLocalDateTime() : null);
            analytics.setClickedAt(rs.getTimestamp(6) != null ? rs.getTimestamp(6).toLocalDateTime() : null);
            analytics.setConvertedAt(rs.getTimestamp(7) != null ? rs.getTimestamp(7).toLocalDateTime() : null);
            analytics.setUnsubscribedAt(rs.getTimestamp(8) != null ? rs.getTimestamp(8).toLocalDateTime() : null);
            analytics.setVariant(rs.getString(9));
            return analytics;
        });
    }

    @Override
    public List<CampaignAnalytics> findByCampaignIdAndCustomerId(Integer campaignId, Integer customerId) {
        String sql = "SELECT id, campaign_id, customer_id, sent_at, opened_at, clicked_at, converted_at, unsubscribed_at, variant " +
                "FROM campaign_analytics WHERE campaign_id = ? AND customer_id = ?";

        return jdbcTemplate.query(sql, new Object[]{campaignId, customerId}, (rs, rowNum) -> {
            CampaignAnalytics analytics = new CampaignAnalytics();
            analytics.setId(rs.getInt(1));
            analytics.setCampaignId(rs.getInt(2));
            analytics.setCustomerId(rs.getInt(3));
            analytics.setSentAt(rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null);
            analytics.setOpenedAt(rs.getTimestamp(5) != null ? rs.getTimestamp(5).toLocalDateTime() : null);
            analytics.setClickedAt(rs.getTimestamp(6) != null ? rs.getTimestamp(6).toLocalDateTime() : null);
            analytics.setConvertedAt(rs.getTimestamp(7) != null ? rs.getTimestamp(7).toLocalDateTime() : null);
            analytics.setUnsubscribedAt(rs.getTimestamp(8) != null ? rs.getTimestamp(8).toLocalDateTime() : null);
            analytics.setVariant(rs.getString(9));
            return analytics;
        });
    }

    @Override
    public Long countOpenedByCampaignId(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND opened_at IS NOT NULL";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public Long countClickedByCampaignId(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND clicked_at IS NOT NULL";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public Long countConvertedByCampaignId(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND converted_at IS NOT NULL";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public Long countUnsubscribedByCampaignId(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND unsubscribed_at IS NOT NULL";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public Long countTotalSentByCampaignId(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public Long countVariantAOpened(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND opened_at IS NOT NULL AND variant = 'A'";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public Long countVariantBOpened(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND opened_at IS NOT NULL AND variant = 'B'";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public Long countVariantAConverted(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND converted_at IS NOT NULL AND variant = 'A'";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public Long countVariantBConverted(Integer campaignId) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND converted_at IS NOT NULL AND variant = 'B'";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public List<CampaignAnalytics> findByCampaignIdAndDateRange(Integer campaignId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT id, campaign_id, customer_id, sent_at, opened_at, clicked_at, converted_at, unsubscribed_at, variant " +
                "FROM campaign_analytics WHERE campaign_id = ? AND sent_at BETWEEN ? AND ?";

        return jdbcTemplate.query(sql, new Object[]{campaignId, startDate, endDate}, (rs, rowNum) -> {
            CampaignAnalytics analytics = new CampaignAnalytics();
            analytics.setId(rs.getInt(1));
            analytics.setCampaignId(rs.getInt(2));
            analytics.setCustomerId(rs.getInt(3));
            analytics.setSentAt(rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null);
            analytics.setOpenedAt(rs.getTimestamp(5) != null ? rs.getTimestamp(5).toLocalDateTime() : null);
            analytics.setClickedAt(rs.getTimestamp(6) != null ? rs.getTimestamp(6).toLocalDateTime() : null);
            analytics.setConvertedAt(rs.getTimestamp(7) != null ? rs.getTimestamp(7).toLocalDateTime() : null);
            analytics.setUnsubscribedAt(rs.getTimestamp(8) != null ? rs.getTimestamp(8).toLocalDateTime() : null);
            analytics.setVariant(rs.getString(9));
            return analytics;
        });
    }

    @Override
    public Long countSentByDateRange(Integer campaignId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT COUNT(*) FROM campaign_analytics WHERE campaign_id = ? AND sent_at BETWEEN ? AND ?";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{campaignId, startDate, endDate}, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public boolean deleteAllByCampaignId(Integer campaignId) {
        String sql = "DELETE FROM campaign_analytics WHERE campaign_id = ?";
        return jdbcTemplate.update(sql, campaignId) > 0;
    }
}

