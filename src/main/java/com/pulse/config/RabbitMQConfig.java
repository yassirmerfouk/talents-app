package com.pulse.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${suggestions.queue}")
    private String suggestionsQueue;
    @Value("${suggestions.exchange}")
    private String suggestionsExchange;
    @Value("${suggestions.routing-key}")
    private String suggestionsRoutingKey;

    @Value("${activation.queue}")
    private String activationQueue;
    @Value("${activation.exchange}")
    private String activationExchange;
    @Value("${activation.routing-key}")
    private String activationRoutingKey;

    @Value("${reset-password.queue}")
    private String resetPasswordQueue;
    @Value("${reset-password.exchange}")
    private String resetPasswordExchange;
    @Value("${reset-password.routing-key}")
    private String resetPasswordRoutingKey;


    @Bean
    public Queue suggestionsQueue(){
        return new Queue(suggestionsQueue);
    }

    @Bean
    public TopicExchange suggestionsExchange(){
        return new TopicExchange(suggestionsExchange);
    }

    @Bean
    public Binding suggestionsBinding(){
        return BindingBuilder
                .bind(suggestionsQueue())
                .to(suggestionsExchange())
                .with(suggestionsRoutingKey);
    }

    @Bean
    public Queue activationQueue(){
        return new Queue(activationQueue);
    }

    @Bean
    public TopicExchange activationExchange(){
        return new TopicExchange(activationExchange);
    }

    @Bean
    public Binding activationBinding(){
        return BindingBuilder
                .bind(activationQueue())
                .to(activationExchange())
                .with(activationRoutingKey);
    }

    @Bean
    public Queue resetPasswordQueue(){
        return new Queue(resetPasswordQueue);
    }

    @Bean
    public TopicExchange resetPasswordExchange(){
        return new TopicExchange(resetPasswordExchange);
    }

    @Bean
    public Binding resetPasswordBinding(){
        return BindingBuilder
                .bind(resetPasswordQueue())
                .to(resetPasswordExchange())
                .with(resetPasswordRoutingKey);
    }

    @Bean
    public MessageConverter jsonConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }
}
