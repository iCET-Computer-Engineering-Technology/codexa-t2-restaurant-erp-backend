package edu.icet.ecom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.notifications")
@Data
public class NotificationProperties {

    private boolean enabled = true;
    private String cron = "0 0 8 * * *";
    private String businessName = "Our Restaurant";
    private boolean auditLogging = true;
    private int maxRetries = 1;
}

