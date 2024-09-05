package com.kropsz.msspotify.listener;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.msspotify.dto.UserTrackList;
import com.kropsz.msspotify.exception.SpotifyApiException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final RedisTemplate<String, Object> redisTemplate;

    @RabbitListener(queues = "tracks.ms-spotify")
    public void receiveMessage(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            UserTrackList newUserTrackList = mapper.readValue(payload, UserTrackList.class);
    
            String key = "userTracks:" + newUserTrackList.getUser();
    
            redisTemplate.opsForValue().set(key, newUserTrackList, 1, TimeUnit.HOURS);
    
        } catch (Exception e) {
            throw new SpotifyApiException("Error receiving tracks");
        }
    }

    @RabbitListener(queues = "suggestion-tracks.ms-spotify")
    public void receiveSuggestionTracks(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            UserTrackList userTrackList = mapper.readValue(payload, UserTrackList.class);

            String key = "suggestionTracks:" + userTrackList.getUser();

            redisTemplate.opsForValue().set(key, userTrackList.getTracks());

        } catch (Exception e) {
            throw new SpotifyApiException("Error receiving suggestion tracks");
        }
    }

}
