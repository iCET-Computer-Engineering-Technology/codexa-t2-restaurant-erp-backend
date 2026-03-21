package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.MarketingCampaign;
import edu.icet.ecom.repository.MarketingCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class MarketingCampaignRepositoryImpl implements MarketingCampaignRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer save(MarketingCampaign campaign) {
        String sql = "INSERT INTO marketing_campaigns (campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, status, created_by, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, campaign.getCampaignName());

            if (campaign.getSegmentId() != null) {
                ps.setInt(2, campaign.getSegmentId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, campaign.getChannel().getDbValue());
            ps.setString(4, campaign.getSubject());
            ps.setString(5, campaign.getBodyTemplate());
            ps.setBoolean(6, campaign.getAbTestEnabled() != null ? campaign.getAbTestEnabled() : false);
            ps.setString(7, campaign.getVariantBBody());
            ps.setObject(8, campaign.getScheduledAt());
            ps.setString(9, campaign.getStatus().getDbValue());

            if (campaign.getCreatedBy() != null) {
                ps.setInt(10, campaign.getCreatedBy());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : null;
    }

    @Override
    public boolean update(MarketingCampaign campaign) {
        String sql = "UPDATE marketing_campaigns SET campaign_name = ?, segment_id = ?, channel = ?, subject = ?, body_template = ?, ab_test_enabled = ?, variant_b_body = ?, scheduled_at = ?, status = ?, sent_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                campaign.getCampaignName(),
                campaign.getSegmentId(),
                campaign.getChannel().getDbValue(),
                campaign.getSubject(),
                campaign.getBodyTemplate(),
                campaign.getAbTestEnabled(),
                campaign.getVariantBBody(),
                campaign.getScheduledAt(),
                campaign.getStatus().getDbValue(),
                campaign.getSentAt(),
                campaign.getId()) > 0;
    }

    @Override
    public Optional<MarketingCampaign> findById(Integer id) {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE id = ?";

        List<MarketingCampaign> result = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            MarketingCampaign campaign = new MarketingCampaign();
            campaign.setId(rs.getInt(1));
            campaign.setCampaignName(rs.getString(2));
            campaign.setSegmentId(rs.getInt(3));
            campaign.setChannel(MarketingCampaign.Channel.fromDbValue(rs.getString(4)));
            campaign.setSubject(rs.getString(5));
            campaign.setBodyTemplate(rs.getString(6));
            campaign.setAbTestEnabled(rs.getBoolean(7));
            campaign.setVariantBBody(rs.getString(8));
            campaign.setScheduledAt(rs.getTimestamp(9) != null ? rs.getTimestamp(9).toLocalDateTime() : null);
            campaign.setSentAt(rs.getTimestamp(10) != null ? rs.getTimestamp(10).toLocalDateTime() : null);
            campaign.setStatus(MarketingCampaign.Status.fromDbValue(rs.getString(11)));
            campaign.setCreatedBy(rs.getInt(12));
            campaign.setCreatedAt(rs.getTimestamp(13) != null ? rs.getTimestamp(13).toLocalDateTime() : null);
            return campaign;
        });

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<MarketingCampaign> findAll() {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MarketingCampaign campaign = new MarketingCampaign();
            campaign.setId(rs.getInt(1));
            campaign.setCampaignName(rs.getString(2));
            campaign.setSegmentId(rs.getInt(3));
            campaign.setChannel(MarketingCampaign.Channel.fromDbValue(rs.getString(4)));
            campaign.setSubject(rs.getString(5));
            campaign.setBodyTemplate(rs.getString(6));
            campaign.setAbTestEnabled(rs.getBoolean(7));
            campaign.setVariantBBody(rs.getString(8));
            campaign.setScheduledAt(rs.getTimestamp(9) != null ? rs.getTimestamp(9).toLocalDateTime() : null);
            campaign.setSentAt(rs.getTimestamp(10) != null ? rs.getTimestamp(10).toLocalDateTime() : null);
            campaign.setStatus(MarketingCampaign.Status.fromDbValue(rs.getString(11)));
            campaign.setCreatedBy(rs.getInt(12));
            campaign.setCreatedAt(rs.getTimestamp(13) != null ? rs.getTimestamp(13).toLocalDateTime() : null);
            return campaign;
        });
    }

    @Override
    public List<MarketingCampaign> findByStatus(String status) {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE status = ?";

        return mapCampaigns(sql, new Object[]{status});
    }

    @Override
    public List<MarketingCampaign> findBySegmentId(Integer segmentId) {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE segment_id = ?";

        return mapCampaigns(sql, new Object[]{segmentId});
    }

    @Override
    public List<MarketingCampaign> findByChannel(String channel) {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE channel = ?";

        return mapCampaigns(sql, new Object[]{channel});
    }

    @Override
    public List<MarketingCampaign> findByCreatedBy(Integer userId) {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE created_by = ?";

        return mapCampaigns(sql, new Object[]{userId});
    }

    @Override
    public List<MarketingCampaign> findByCampaignNameContaining(String name) {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE LOWER(campaign_name) LIKE LOWER(?)";

        return mapCampaigns(sql, new Object[]{"%" + name + "%"});
    }

    @Override
    public List<MarketingCampaign> findByScheduledDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE scheduled_at BETWEEN ? AND ?";

        return mapCampaigns(sql, new Object[]{startDate, endDate});
    }

    @Override
    public List<MarketingCampaign> findAllSentCampaigns() {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE status = 'sent' AND sent_at IS NOT NULL";

        return mapCampaigns(sql, new Object[]{});
    }

    @Override
    public List<MarketingCampaign> findActiveCampaigns() {
        String sql = "SELECT id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled, variant_b_body, scheduled_at, sent_at, status, created_by, created_at " +
                "FROM marketing_campaigns WHERE status IN ('scheduled', 'sent')";

        return mapCampaigns(sql, new Object[]{});
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM marketing_campaigns WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    private List<MarketingCampaign> mapCampaigns(String sql, Object[] params) {
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            MarketingCampaign campaign = new MarketingCampaign();
            campaign.setId(rs.getInt(1));
            campaign.setCampaignName(rs.getString(2));
            campaign.setSegmentId(rs.getInt(3));
            campaign.setChannel(MarketingCampaign.Channel.fromDbValue(rs.getString(4)));
            campaign.setSubject(rs.getString(5));
            campaign.setBodyTemplate(rs.getString(6));
            campaign.setAbTestEnabled(rs.getBoolean(7));
            campaign.setVariantBBody(rs.getString(8));
            campaign.setScheduledAt(rs.getTimestamp(9) != null ? rs.getTimestamp(9).toLocalDateTime() : null);
            campaign.setSentAt(rs.getTimestamp(10) != null ? rs.getTimestamp(10).toLocalDateTime() : null);
            campaign.setStatus(MarketingCampaign.Status.fromDbValue(rs.getString(11)));
            campaign.setCreatedBy(rs.getInt(12));
            campaign.setCreatedAt(rs.getTimestamp(13) != null ? rs.getTimestamp(13).toLocalDateTime() : null);
            return campaign;
        });
    }
}

