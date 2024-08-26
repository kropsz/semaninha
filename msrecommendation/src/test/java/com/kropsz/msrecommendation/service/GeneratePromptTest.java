package com.kropsz.msrecommendation.service;

import com.kropsz.msrecommendation.entity.Artist;
import com.kropsz.msrecommendation.entity.Tracks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneratePromptTest {

    @Test
    @DisplayName("Should generate a prompt with the list of songs")
    void testPrompt() {
        Artist artist1 = new Artist("Artist1");
        Artist artist2 = new Artist("Artist2");

        Tracks track1 = new Tracks("Song1", artist1);
        Tracks track2 = new Tracks("Song2", artist2);

        List<Tracks> tracksList = new ArrayList<>();
        tracksList.add(track1);
        tracksList.add(track2);

        GeneratePrompt promptGenerator = new GeneratePrompt();

        String result = promptGenerator.prompt(tracksList);

        String expected = """
                Hello! I have a list of songs that I like, and I would like to receive recommendations for similar songs. Please consider similarity based on both the style of the music and the artists. Here are the songs:

                Song1 - Artist1
                Song2 - Artist2
                
                
                I would like the recommendations to include:

                - Songs that share stylistic or genre elements with the listed songs.
                - Artists who have a sound or musical influences similar to the mentioned artists.
                - Please include a mix of well-known tracks and some hidden gems.
                - I would like you to send 2 recommendations.

                (Your response should be just the list of recommended songs and nothing more. Place one after the other following this example:

                Song1 - Artist1
                Song2 - Artist2

                Just that in your response.)
                
                """;

        assertEquals(expected, result);
    }

}
