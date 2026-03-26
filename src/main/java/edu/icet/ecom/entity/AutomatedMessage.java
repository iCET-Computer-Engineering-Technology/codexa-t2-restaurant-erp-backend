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
public class AutomatedMessage {
    private Integer id;
    private TriggerType triggerType;
    private Channel channel;
    private String templateBody;
    private OfferType offerType;
    private Double offerValue;
    private Integer sendDaysBefore;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public enum TriggerType {
        BIRTHDAY("birthday"),
        ANNIVERSARY("anniversary"),
        LAPSED("lapsed"),
        TIER_CHANGE("tier_change");

        private final String dbValue;

        TriggerType(String dbValue) {
            this.dbValue = dbValue;
        }

        public static TriggerType fromDbValue(String value) {
            for (TriggerType type : values()) {
                if (type.dbValue.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown trigger type: " + value);
        }
    }

    public enum Channel {
        EMAIL("email"),
        SMS("sms");

        private final String dbValue;

        Channel(String dbValue) {
            this.dbValue = dbValue;
        }

        public static Channel fromDbValue(String value) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("Unknown channel: " + value);
            }
            // MySQL SET columns can return comma-separated values; use the first supported value.
            String normalized = value.contains(",") ? value.split(",")[0].trim() : value.trim();
            for (Channel ch : values()) {
                if (ch.dbValue.equalsIgnoreCase(normalized)) {
                    return ch;
                }
            }
            throw new IllegalArgumentException("Unknown channel: " + value);
        }
    }

    public enum OfferType {
        DISCOUNT("discount"),
        FREE_ITEM("free_item"),
        NONE("none");

        private final String dbValue;

        OfferType(String dbValue) {
            this.dbValue = dbValue;
        }

        public static OfferType fromDbValue(String value) {
            for (OfferType type : values()) {
                if (type.dbValue.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown offer type: " + value);
        }
    }

    public Integer getSendDaysBeforeWithDefault() {
        return sendDaysBefore != null ? sendDaysBefore : 1;
    }

    public boolean hasDiscount() {
        return offerType == OfferType.DISCOUNT && offerValue != null && offerValue > 0;
    }

    public boolean isActiveRule() {
        return Boolean.TRUE.equals(isActive);
    }
}


