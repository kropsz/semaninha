package com.kropsz.msrecommendation.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.msrecommendation.dto.UserTrackList;
import com.kropsz.msrecommendation.exceptions.JsonConverterException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.suggestion.queue}")
    private String suggestionTracksQueue;

    @Value("${rabbitmq.suggestion.exchange}")
    private String exchange;

    public void publish(UserTrackList payload) throws JsonProcessingException {
        String jsonTracks = convertToJson(payload);
        rabbitTemplate.convertAndSend(exchange, suggestionTracksQueue, jsonTracks);
    }

    public String convertToJson(UserTrackList payload) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new JsonConverterException("Error converting tracks to JSON");
        }
    }
}
