package com.pulse.amqp.producer;

import com.pulse.dto.suggestion.Suggestion;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuggestionProducer {

    @Value("${suggestions.exchange}")
    private String suggestionsExchange;
    @Value("${suggestions.routing-key}")
    private String suggestionsRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendSuggestion(Suggestion suggestion){
        rabbitTemplate.convertAndSend(suggestionsExchange, suggestionsRoutingKey, suggestion);
    }
}
