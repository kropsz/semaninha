package com.kropsz.msrecommendation.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.msrecommendation.dto.UserTrackList;
import com.kropsz.msrecommendation.exceptions.JsonConverterException;
import com.kropsz.msrecommendation.service.AIRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackListener {

    private final AIRecommendationService aiRecommendationService;

    @RabbitListener(queues = "tracks.ms-ai")
    public void listen(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            UserTrackList userTrackList = mapper.readValue(payload, UserTrackList.class);
            aiRecommendationService.generateRecommendation(userTrackList);
        } catch (Exception e) {
            throw new JsonConverterException("Error receiving tracks");
        }
    }
}
