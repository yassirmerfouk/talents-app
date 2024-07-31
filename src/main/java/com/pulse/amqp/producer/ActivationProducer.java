package com.pulse.amqp.producer;

import com.pulse.dto.email.Activation;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationProducer {

    @Value("${activation.exchange}")
    private String activationExchange;
    @Value("${activation.routing-key}")
    private String activationRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendActivation(Activation activation){
        rabbitTemplate.convertAndSend(activationExchange, activationRoutingKey, activation);
    }


}
