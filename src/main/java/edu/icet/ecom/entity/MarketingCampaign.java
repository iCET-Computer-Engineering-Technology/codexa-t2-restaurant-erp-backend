package edu.icet.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketingCampaign {
    private Integer id;
    private String campaignName;
    private Integer segmentId;
    private Channel channel;
    private String subject;
    private String bodyTemplate;
    private Boolean abTestEnabled;
    private String variantBBody;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private Status status;
    private Integer createdBy;
    private LocalDateTime createdAt;

    public enum Channel {
        EMAIL("email"),
        SMS("sms");

        private final String dbValue;

        Channel(String dbValue) {
            this.dbValue = dbValue;
        }

        public static Channel fromDbValue(String value) {
            for (Channel ch : values()) {
                if (ch.dbValue.equalsIgnoreCase(value)) {
                    return ch;
                }
            }
            throw new IllegalArgumentException("Unknown channel: " + value);
        }

        public String getDbValue() {
            return dbValue;
        }
    }

    public enum Status {
        DRAFT("draft"),
        SCHEDULED("scheduled"),
        SENT("sent"),
        CANCELLED("cancelled");

        private final String dbValue;

        Status(String dbValue) {
            this.dbValue = dbValue;
        }

        public static Status fromDbValue(String value) {
            for (Status status : values()) {
                if (status.dbValue.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status: " + value);
        }

        public String getDbValue() {
            return dbValue;
        }
    }
}

