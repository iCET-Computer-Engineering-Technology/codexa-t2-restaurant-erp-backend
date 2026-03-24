package edu.icet.ecom.service;

import edu.icet.ecom.dto.SendEmailRequest;
import edu.icet.ecom.dto.MarketingEmailRequest;

public interface EmailService {

    int sendEmailToAllCustomers(SendEmailRequest request);
    void sendEmailToCustomer(String email, String subject, String body);
    int sendEmailToBirthdayCustomers(SendEmailRequest request);
    int sendEmailToAnniversaryCustomers(SendEmailRequest request);
    boolean sendMarketingEmail(MarketingEmailRequest request);
}

