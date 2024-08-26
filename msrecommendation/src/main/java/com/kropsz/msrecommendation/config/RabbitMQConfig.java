package com.kropsz.msrecommendation.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.suggestion.exchange}")
    private String suggestionTracksExchange;

    @Value("${rabbitmq.suggestion.queue}")
    private String suggestionTracksQueue;

    @Bean
    public Queue createQueueToSuggestionTracks() {
        return new Queue(suggestionTracksQueue, true);
    }

    @Bean
    public DirectExchange createDirectExchange() {
        return new DirectExchange(suggestionTracksExchange);
    }

    @Bean
    public Binding createBindingSuggestionTracks() {
        return BindingBuilder.bind(createQueueToSuggestionTracks()).to(createDirectExchange()).withQueueName();
    }

}
