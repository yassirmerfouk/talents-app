package com.pulse.service.email;

import com.pulse.enumeration.EmailTemplateName;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface EmailService {
    @Async
    void sendEmail(
            String to,
            EmailTemplateName emailTemplateName,
            String subject,
            Map<String, Object> properties
    ) throws MessagingException;

    void sendSimpleEmail(
            String to,
            String subject,
            String body
    ) throws RuntimeException;
}
