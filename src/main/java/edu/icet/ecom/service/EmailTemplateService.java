package edu.icet.ecom.service;

import java.util.Map;

public interface EmailTemplateService {

    String getPromotionalEmailTemplate();
    String getBirthdayEmailTemplate();
    String getAnniversaryEmailTemplate();
    String renderTemplate(String template, Map<String, String> placeholders);
    String getMarketingCampaignTemplate();
}

