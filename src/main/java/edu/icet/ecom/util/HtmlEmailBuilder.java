package edu.icet.ecom.util;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HtmlEmailBuilder {

    /** Wrap plain text in a simple HTML container */
    public String wrapInHtml(String plainText, String senderName) {
        return String.format(
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "</head>\n" +
            "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">\n" +
            "  <div style=\"max-width: 600px; margin: 0 auto; padding: 20px;\">\n" +
            "    <div style=\"background-color: #FF6B35; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0;\">\n" +
            "      <h2 style=\"margin: 0;\">%s</h2>\n" +
            "    </div>\n" +
            "    <div style=\"background-color: #ffffff; padding: 20px; border: 1px solid #ddd; border-radius: 0 0 8px 8px;\">\n" +
            "      %s\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</body>\n" +
            "</html>",
            senderName,
            plainText
        );
    }

    /** Build a promotional banner HTML */
    public String buildPromoBanner(String title, String description, String ctaText, String ctaUrl) {
        return String.format(
            "<div style=\"background-color: #FFF3E0; border-left: 4px solid #FF6B35; padding: 15px; margin: 20px 0; border-radius: 4px;\">\n" +
            "  <h3 style=\"color: #FF6B35; margin-top: 0;\">%s</h3>\n" +
            "  <p>%s</p>\n" +
            "  <a href=\"%s\" style=\"display: inline-block; background-color: #FF6B35; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px;\">%s</a>\n" +
            "</div>",
            title, description, ctaUrl, ctaText
        );
    }

    /** Build a greeting with customer name */
    public String buildGreeting(String customerName) {
        return String.format(
            "<p style=\"margin-bottom: 20px;\">Hello <strong>%s</strong>,</p>",
            customerName != null ? customerName : "Valued Customer"
        );
    }

    /** Build footer with company info */
    public String buildFooter() {
        return "<div style=\"border-top: 1px solid #ddd; margin-top: 30px; padding-top: 20px; font-size: 12px; color: #666;\">\n" +
            "  <p><strong>Restaurant ERP</strong></p>\n" +
            "  <p>\n" +
            "    📧 support@restauranterp.com | 📞 +1 (555) 123-4567<br>\n" +
            "    🌐 www.restauranterp.com\n" +
            "  </p>\n" +
            "  <p><a href=\"{{unsubscribeLink}}\" style=\"color: #FF6B35; text-decoration: none;\">Unsubscribe</a></p>\n" +
            "</div>";
    }

    /** Convert plain text message to HTML with basic formatting */
    public String toHtml(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return "<p>No content</p>";
        }

        return "<p style=\"line-height: 1.8; color: #555;\">" +
            plainText.replace("\n", "<br>") +
            "</p>";
    }
}


