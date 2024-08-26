package com.kropsz.msrecommendation.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kropsz.msrecommendation.dto.UserTrackList;
import com.kropsz.msrecommendation.entity.Artist;
import com.kropsz.msrecommendation.entity.Tracks;
import com.kropsz.msrecommendation.publisher.RabbitMQPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIRecommendationService {

    private final AnthropicChatModel chatModel;
    private final GeneratePrompt generatePrompt;
    private final RabbitMQPublisher rabbitMQPublisher;

    public void generateRecommendation(UserTrackList userTrackList) throws JsonProcessingException {
        String response = chatModel.call(generatePrompt.prompt(userTrackList.getTracks()));
        List<Tracks> recommendedTracks = parseResponse(response);
        var payload = new UserTrackList(userTrackList.getUser(), recommendedTracks, null);
        rabbitMQPublisher.publish(payload);
    }

    public List<Tracks> parseResponse(String response) {
        List<Tracks> trackList = new ArrayList<>();

        if (response == null || response.trim().isEmpty()) {
            return trackList;
        }

        String[] lines = response.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(" - ");
            if (parts.length == 2) {
                String nome = parts[0].trim();
                String artistName = parts[1].trim();
                Artist artist = new Artist(artistName);
                Tracks track = new Tracks(nome, artist);
                trackList.add(track);
            }
        }
        return trackList;
    }
}
