package com.pulse.service.email;

import com.pulse.enumeration.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    @Async
    public void sendEmail(
            String to,
            EmailTemplateName emailTemplateName,
            String subject,
            Map<String, Object> properties
    ) throws MessagingException {

        String templateName = emailTemplateName.getName();

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(
                        mimeMessage,
                        MimeMessageHelper.MULTIPART_MODE_MIXED,
                        "UTF-8");

        mimeMessageHelper.setFrom("pulse.digital@mail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        Context context = new Context();
        context.setVariables(properties);

        String template = templateEngine.process(templateName, context);

        mimeMessageHelper.setText(template, true);

        mailSender.send(mimeMessage);
    }


    @Override
    @Async
    public void sendSimpleEmail(
            String to,
            String subject,
            String body
    ) throws RuntimeException{

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setFrom("pulse.digital@mail.com");
        message.setTo(to);
        message.setText(body);
        mailSender.send(message);
    }
}
