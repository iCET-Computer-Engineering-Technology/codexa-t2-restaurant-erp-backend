package edu.icet.ecom.service;

public interface NotificationService {
    void sendEmail(String to, String subject, String body);
    void sendSms(String to, String body);
}

