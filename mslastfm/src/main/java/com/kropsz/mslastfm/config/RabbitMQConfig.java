package com.kropsz.mslastfm.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.tracks.exchange}")
    private String recentTracksExchange;

    @Bean
    public Queue createQueueToRecentTracksSpotify() {
        return new Queue("tracks.ms-spotify", true);
    }

    @Bean
    public Queue createQueueToRecentTracksAi() {
        return new Queue("tracks.ms-ai", true);
    }

    @Bean
    public FanoutExchange createFanoutExchange() {
        return new FanoutExchange(recentTracksExchange);
    }

    @Bean
    public Binding createBidingSpotify(){
        return BindingBuilder.bind(createQueueToRecentTracksSpotify()).to(createFanoutExchange());
    }

    @Bean
    public Binding createBidingAi(){
        return BindingBuilder.bind(createQueueToRecentTracksAi()).to(createFanoutExchange());
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> createApplicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }


}