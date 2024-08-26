package com.kropsz.msrecommendation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.msrecommendation.entity.Artist;
import com.kropsz.msrecommendation.entity.Tracks;

@ExtendWith(MockitoExtension.class)
class AIRecommendationServiceTest {

    @InjectMocks
    private AIRecommendationService aiRecommendationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testParseResponse() {
        String response = "Song1 - Artist1\nSong2 - Artist2\n";

        List<Tracks> result = aiRecommendationService.parseResponse(response);

        List<Tracks> expectedTracks = List.of(
                new Tracks("Song1", new Artist("Artist1")),
                new Tracks("Song2", new Artist("Artist2"))
        );

        assertEquals(expectedTracks, result);
    }
}
