package com.pulse.amqp.producer;

import com.pulse.dto.email.ResetPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordProducer {

    @Value("${reset-password.exchange}")
    private String resetPasswordExchange;
    @Value("${reset-password.routing-key}")
    private String resetPasswordRoutingKey;

    public final RabbitTemplate rabbitTemplate;

    public void sendResetPassword(ResetPassword resetPassword){
        rabbitTemplate.convertAndSend(resetPasswordExchange, resetPasswordRoutingKey, resetPassword);
    }
}
