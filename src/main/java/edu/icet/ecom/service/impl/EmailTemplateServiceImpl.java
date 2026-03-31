package edu.icet.ecom.service.impl;

import edu.icet.ecom.service.EmailTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private static final String RESTAURANT_NAME = "Restaurant ERP";
    private static final String RESTAURANT_LOGO_URL = "https://via.placeholder.com/200x80/FF6B35/FFFFFF?text=RestaurantERP";
    private static final String COMPANY_WEBSITE = "www.restauranterp.com";
    private static final String COMPANY_PHONE = "+1 (555) 123-4567";
    private static final String COMPANY_EMAIL = "support@restauranterp.com";

    @Override
    public String getPromotionalEmailTemplate() {
        return buildBaseTemplate(
            "Special Offer Just For You!",
            "{{discount}}",
            "Claim Offer",
            "/offers/redeem"
        );
    }

    @Override
    public String getBirthdayEmailTemplate() {
        return buildBaseTemplate(
            "Happy Birthday, {{name}}! 🎉",
            "Enjoy our exclusive {{discount}}% birthday discount on your next visit!",
            "Claim Birthday Offer",
            "/offers/birthday"
        );
    }

    @Override
    public String getAnniversaryEmailTemplate() {
        return buildBaseTemplate(
            "Happy Anniversary with {{restaurant}}! 🎊",
            "Thank you for {{years}} wonderful years! Here's {{discount}}% off for our valued customer.",
            "Claim Anniversary Offer",
            "/offers/anniversary"
        );
    }

    @Override
    public String getMarketingCampaignTemplate() {
        return buildBaseTemplate(
            "{{campaignName}}",
            "{{message}}",
            "Learn More",
            "/campaigns/{{campaignId}}"
        );
    }

    @Override
    public String getReservationReminderEmailTemplate() {
        // More eye-catching, modern HTML design for reservation reminders
        return """
<!DOCTYPE html>
<html lang=\"en\">
<head>
    <meta charset=\"UTF-8\">
    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
    <title>Reservation Reminder</title>
    <style>
        body { background: linear-gradient(135deg, #f8fafc 0%, #e0e7ff 100%); font-family: 'Segoe UI', Arial, sans-serif; margin: 0; padding: 0; }
        .container { max-width: 540px; margin: 40px auto; background: #fff; border-radius: 18px; box-shadow: 0 8px 32px rgba(44,62,80,0.13); overflow: hidden; }
        .header { background: linear-gradient(90deg, #ff6b35 0%, #f7931e 100%); color: #fff; padding: 36px 0 18px 0; text-align: center; position: relative; }
        .header img { height: 70px; margin-bottom: 10px; filter: drop-shadow(0 2px 8px rgba(0,0,0,0.12)); }
        .header h1 { margin: 0; font-size: 2.3rem; letter-spacing: 1px; font-weight: 700; text-shadow: 0 2px 8px rgba(0,0,0,0.10); }
        .confetti { position: absolute; left: 0; right: 0; top: 0; height: 100%; pointer-events: none; z-index: 1; }
        .content { padding: 36px 36px 24px 36px; text-align: left; }
        .greeting { font-size: 1.15rem; color: #ff6b35; margin-bottom: 16px; font-weight: 500; }
        .reminder-box { background: #fff7ed; border-left: 6px solid #ff6b35; padding: 20px 22px; border-radius: 8px; margin-bottom: 22px; box-shadow: 0 2px 8px rgba(255,107,53,0.07); }
        .reminder-title { font-size: 1.25rem; font-weight: bold; color: #ff6b35; margin-bottom: 8px; }
        .details { margin: 22px 0; }
        .details-row { margin-bottom: 10px; }
        .details-label { font-weight: 600; color: #f7931e; display: inline-block; width: 120px; }
        .details-value { color: #333; font-weight: 500; }
        .cta-btn { display: inline-block; background: linear-gradient(90deg, #ff6b35 0%, #f7931e 100%); color: #fff; text-decoration: none; padding: 14px 38px; border-radius: 32px; font-size: 1.08rem; font-weight: bold; margin: 22px 0; transition: background 0.2s, box-shadow 0.2s; box-shadow: 0 4px 16px rgba(255,107,53,0.13); letter-spacing: 0.5px; }
        .cta-btn:hover { background: linear-gradient(90deg, #f7931e 0%, #ff6b35 100%); box-shadow: 0 6px 24px rgba(255,107,53,0.18); }
        .footer { background: #f8fafc; color: #888; text-align: center; font-size: 1rem; padding: 20px 0 14px 0; border-top: 1px solid #f3f3f3; }
        .footer a { color: #ff6b35; text-decoration: none; }
        .footer a:hover { text-decoration: underline; }
        @media (max-width: 600px) { .container { border-radius: 0; } .content { padding: 18px 8px; } }
    </style>
</head>
<body>
    <div class=\"container\">
        <div class=\"header\">
            <img src=\"https://cdn-icons-png.flaticon.com/512/2922/2922506.png\" alt=\"Reservation\">
            <h1>Reservation Reminder</h1>
            <div class=\"confetti\">
                <img src=\"https://cdn.pixabay.com/photo/2013/07/12/13/58/confetti-147615_1280.png\" alt=\"confetti\" style=\"width:100%;opacity:0.13;\">
            </div>
        </div>
        <div class=\"content\">
            <div class=\"greeting\">Hello {{customerName}},</div>
            <div class=\"reminder-box\">
                <div class=\"reminder-title\">🎉 Your reservation is almost here!</div>
                <div>We're excited to welcome you at <b>Restaurant ERP</b>. Get ready for a great experience!</div>
            </div>
            <div class=\"details\">
                <div class=\"details-row\"><span class=\"details-label\">Date:</span> <span class=\"details-value\">{{reservationDate}}</span></div>
                <div class=\"details-row\"><span class=\"details-label\">Time:</span> <span class=\"details-value\">{{reservationTime}}</span></div>
                <div class=\"details-row\"><span class=\"details-label\">Table:</span> <span class=\"details-value\">{{tableNumber}}</span></div>
                <div class=\"details-row\"><span class=\"details-label\">Party Size:</span> <span class=\"details-value\">{{partySize}} {{partyLabel}}</span></div>
                <div class=\"details-row\"><span class=\"details-label\">Confirmation:</span> <span class=\"details-value\">{{confirmationCode}}</span></div>
            </div>
            {{notesSection}}
            <a href=\"https://www.restauranterp.com/reservations/view\" class=\"cta-btn\">View Reservation</a>
        </div>
        <div class=\"footer\">
            For questions, contact us at <a href=\"mailto:support@restauranterp.com\">support@restauranterp.com</a> or call +1 (555) 123-4567.<br>
            &copy; 2026 Restaurant ERP. All rights reserved.
        </div>
    </div>
</body>
</html>
""";
    }

    @Override
    public String renderTemplate(String template, Map<String, String> placeholders) {
        String rendered = template;
        if (placeholders != null && !placeholders.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                String value = entry.getValue() != null ? entry.getValue() : "";
                rendered = rendered.replace(placeholder, value);
            }
            log.debug("Template rendered successfully with {} placeholders", placeholders.size());
        }
        return rendered;
    }

    private String buildBaseTemplate(String title, String message, String ctaButtonText, String ctaUrl) {
        String baseTemplate = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>{{restaurant}} - Special Offer</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        body {\n" +
            "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
            "            background-color: #f5f5f5;\n" +
            "            color: #333;\n" +
            "            line-height: 1.6;\n" +
            "        }\n" +
            "        .email-container {\n" +
            "            max-width: 600px;\n" +
            "            margin: 0 auto;\n" +
            "            background-color: #ffffff;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);\n" +
            "            overflow: hidden;\n" +
            "        }\n" +
            "        .header {\n" +
            "            background: linear-gradient(135deg, #FF6B35 0%, #F7931E 100%);\n" +
            "            padding: 30px;\n" +
            "            text-align: center;\n" +
            "            color: white;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            height: 80px;\n" +
            "            width: auto;\n" +
            "            margin-bottom: 10px;\n" +
            "        }\n" +
            "        .header-text {\n" +
            "            font-size: 14px;\n" +
            "            opacity: 0.95;\n" +
            "        }\n" +
            "        .content {\n" +
            "            padding: 40px 30px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        .greeting {\n" +
            "            font-size: 14px;\n" +
            "            color: #666;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "        .title {\n" +
            "            font-size: 32px;\n" +
            "            font-weight: bold;\n" +
            "            color: #FF6B35;\n" +
            "            margin-bottom: 15px;\n" +
            "            line-height: 1.3;\n" +
            "        }\n" +
            "        .message {\n" +
            "            font-size: 16px;\n" +
            "            color: #555;\n" +
            "            margin-bottom: 30px;\n" +
            "            line-height: 1.8;\n" +
            "        }\n" +
            "        .offer-highlight {\n" +
            "            font-size: 48px;\n" +
            "            font-weight: bold;\n" +
            "            color: #FF6B35;\n" +
            "            margin: 20px 0;\n" +
            "            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);\n" +
            "        }\n" +
            "        .cta-button {\n" +
            "            display: inline-block;\n" +
            "            padding: 14px 40px;\n" +
            "            background: linear-gradient(135deg, #FF6B35 0%, #F7931E 100%);\n" +
            "            color: white;\n" +
            "            text-decoration: none;\n" +
            "            border-radius: 50px;\n" +
            "            font-size: 16px;\n" +
            "            font-weight: bold;\n" +
            "            margin: 20px 0;\n" +
            "            transition: all 0.3s ease;\n" +
            "            box-shadow: 0 4px 12px rgba(255, 107, 53, 0.4);\n" +
            "        }\n" +
            "        .cta-button:hover {\n" +
            "            transform: translateY(-2px);\n" +
            "            box-shadow: 0 6px 16px rgba(255, 107, 53, 0.6);\n" +
            "        }\n" +
            "        .offer-box {\n" +
            "            background-color: #FFF3E0;\n" +
            "            border-left: 4px solid #FF6B35;\n" +
            "            padding: 20px;\n" +
            "            margin: 25px 0;\n" +
            "            border-radius: 4px;\n" +
            "        }\n" +
            "        .offer-box-title {\n" +
            "            font-size: 14px;\n" +
            "            font-weight: bold;\n" +
            "            color: #FF6B35;\n" +
            "            margin-bottom: 8px;\n" +
            "        }\n" +
            "        .offer-box-text {\n" +
            "            font-size: 14px;\n" +
            "            color: #666;\n" +
            "        }\n" +
            "        .features {\n" +
            "            display: table;\n" +
            "            width: 100%;\n" +
            "            margin: 30px 0;\n" +
            "            border-collapse: collapse;\n" +
            "        }\n" +
            "        .feature {\n" +
            "            display: table-cell;\n" +
            "            padding: 15px;\n" +
            "            text-align: center;\n" +
            "            border-right: 1px solid #eee;\n" +
            "        }\n" +
            "        .feature:last-child {\n" +
            "            border-right: none;\n" +
            "        }\n" +
            "        .feature-icon {\n" +
            "            font-size: 24px;\n" +
            "            margin-bottom: 10px;\n" +
            "        }\n" +
            "        .feature-text {\n" +
            "            font-size: 12px;\n" +
            "            color: #666;\n" +
            "        }\n" +
            "        .divider {\n" +
            "            height: 1px;\n" +
            "            background-color: #eee;\n" +
            "            margin: 30px 0;\n" +
            "        }\n" +
            "        .footer {\n" +
            "            background-color: #f9f9f9;\n" +
            "            padding: 30px;\n" +
            "            text-align: center;\n" +
            "            border-top: 1px solid #eee;\n" +
            "        }\n" +
            "        .footer-title {\n" +
            "            font-size: 14px;\n" +
            "            font-weight: bold;\n" +
            "            color: #333;\n" +
            "            margin-bottom: 12px;\n" +
            "        }\n" +
            "        .footer-contact {\n" +
            "            font-size: 13px;\n" +
            "            color: #666;\n" +
            "            margin-bottom: 8px;\n" +
            "        }\n" +
            "        .footer-links {\n" +
            "            margin-top: 15px;\n" +
            "        }\n" +
            "        .footer-link {\n" +
            "            display: inline-block;\n" +
            "            margin: 0 10px;\n" +
            "            text-decoration: none;\n" +
            "            color: #FF6B35;\n" +
            "            font-size: 12px;\n" +
            "        }\n" +
            "        .footer-link:hover {\n" +
            "            text-decoration: underline;\n" +
            "        }\n" +
            "        .social-icons {\n" +
            "            margin: 15px 0;\n" +
            "        }\n" +
            "        .social-icon {\n" +
            "            display: inline-block;\n" +
            "            width: 32px;\n" +
            "            height: 32px;\n" +
            "            background-color: #FF6B35;\n" +
            "            border-radius: 50%;\n" +
            "            text-align: center;\n" +
            "            line-height: 32px;\n" +
            "            margin: 0 5px;\n" +
            "            text-decoration: none;\n" +
            "            color: white;\n" +
            "            font-size: 14px;\n" +
            "        }\n" +
            "        .social-icon:hover {\n" +
            "            background-color: #F7931E;\n" +
            "        }\n" +
            "        .expiration {\n" +
            "            margin-top: 20px;\n" +
            "            font-size: 12px;\n" +
            "            color: #999;\n" +
            "            font-style: italic;\n" +
            "        }\n" +
            "        @media (max-width: 600px) {\n" +
            "            .email-container { border-radius: 0; }\n" +
            "            .header { padding: 20px; }\n" +
            "            .content { padding: 20px; }\n" +
            "            .title { font-size: 24px; }\n" +
            "            .offer-highlight { font-size: 36px; }\n" +
            "            .message { font-size: 14px; }\n" +
            "            .features { display: block; }\n" +
            "            .feature { display: block; padding: 15px 0; border-right: none; border-bottom: 1px solid #eee; }\n" +
            "            .feature:last-child { border-bottom: none; }\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"email-container\">\n" +
            "        <div class=\"header\">\n" +
            "            <img src=\"" + RESTAURANT_LOGO_URL + "\" alt=\"Restaurant ERP\" class=\"logo\">\n" +
            "            <div class=\"header-text\">Experience Exceptional Dining</div>\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <div class=\"greeting\">Hello {{name}},</div>\n" +
            "            <div class=\"title\">" + title + "</div>\n" +
            "            <div class=\"message\">" + message + "</div>\n" +
            "            <div class=\"features\">\n" +
            "                <div class=\"feature\">\n" +
            "                    <div class=\"feature-icon\">🍽️</div>\n" +
            "                    <div class=\"feature-text\">Premium Quality</div>\n" +
            "                </div>\n" +
            "                <div class=\"feature\">\n" +
            "                    <div class=\"feature-icon\">⭐</div>\n" +
            "                    <div class=\"feature-text\">5-Star Experience</div>\n" +
            "                </div>\n" +
            "                <div class=\"feature\">\n" +
            "                    <div class=\"feature-icon\">🚀</div>\n" +
            "                    <div class=\"feature-text\">Fast Delivery</div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "            <a href=\"" + COMPANY_WEBSITE + ctaUrl + "\" class=\"cta-button\">" + ctaButtonText + "</a>\n" +
            "            <div class=\"offer-box\">\n" +
            "                <div class=\"offer-box-title\">⏰ Limited Time Offer</div>\n" +
            "                <div class=\"offer-box-text\">Don't miss out! This exclusive offer is only available for a limited time. Claim your offer today!</div>\n" +
            "            </div>\n" +
            "            <p class=\"expiration\">Valid until: {{expiryDate}}</p>\n" +
            "        </div>\n" +
            "        <div class=\"divider\"></div>\n" +
            "        <div class=\"footer\">\n" +
            "            <div class=\"footer-title\">" + RESTAURANT_NAME + "</div>\n" +
            "            <div class=\"footer-contact\">📧 Email: " + COMPANY_EMAIL + "</div>\n" +
            "            <div class=\"footer-contact\">📞 Phone: " + COMPANY_PHONE + "</div>\n" +
            "            <div class=\"footer-contact\">🌐 Website: " + COMPANY_WEBSITE + "</div>\n" +
            "            <div class=\"social-icons\">\n" +
            "                <a href=\"https://facebook.com/restauranterp\" class=\"social-icon\" title=\"Facebook\">f</a>\n" +
            "                <a href=\"https://instagram.com/restauranterp\" class=\"social-icon\" title=\"Instagram\">📷</a>\n" +
            "                <a href=\"https://twitter.com/restauranterp\" class=\"social-icon\" title=\"Twitter\">𝕏</a>\n" +
            "            </div>\n" +
            "            <div class=\"footer-links\">\n" +
            "                <a href=\"" + COMPANY_WEBSITE + "/privacy\" class=\"footer-link\">Privacy Policy</a>\n" +
            "                <a href=\"" + COMPANY_WEBSITE + "/terms\" class=\"footer-link\">Terms</a>\n" +
            "                <a href=\"" + COMPANY_WEBSITE + "/contact\" class=\"footer-link\">Contact</a>\n" +
            "            </div>\n" +
            "            <p style=\"margin-top: 20px; font-size: 11px; color: #aaa;\">\n" +
            "                You received this email because you are a valued customer of " + RESTAURANT_NAME + ".\n" +
            "                <br>\n" +
            "                <a href=\"{{unsubscribeLink}}\" style=\"color: #aaa; text-decoration: underline;\">Unsubscribe</a>\n" +
            "            </p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

        return baseTemplate;
    }
}
