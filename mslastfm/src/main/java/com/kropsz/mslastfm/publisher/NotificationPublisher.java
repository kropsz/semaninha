package com.kropsz.mslastfm.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.mslastfm.dto.UserTrackList;
import com.kropsz.mslastfm.exception.JsonConverterException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.tracks}")
    private String exchange;

    public void notify(UserTrackList payload) {
        String jsonTracks = convertToJson(payload);
        rabbitTemplate.convertAndSend(exchange, "", jsonTracks);
    }

    public String convertToJson(UserTrackList payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new JsonConverterException("Error converting tracks to JSON");
        }
    }

}