package com.kropsz.msrecommendation.service;

import com.kropsz.msrecommendation.entity.Tracks;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneratePrompt {

    public String prompt(List<Tracks> tracksList) {
        StringBuilder musicDetails = new StringBuilder();
        for (Tracks track : tracksList) {
            musicDetails.append(track.getName()).append(" - ").append(track.getArtist().getName()).append("\n");
        }
        return String.format(
                """
                        Hello! I have a list of songs that I like, and I would like to receive recommendations for similar songs. Please consider similarity based on both the style of the music and the artists. Here are the songs:

                        %s

                        I would like the recommendations to include:

                        - Songs that share stylistic or genre elements with the listed songs.
                        - Artists who have a sound or musical influences similar to the mentioned artists.
                        - Please include a mix of well-known tracks and some hidden gems.
                        - I would like you to send %s recommendations.

                        (Your response should be just the list of recommended songs and nothing more. Place one after the other following this example:

                        Song1 - Artist1
                        Song2 - Artist2

                        Just that in your response.)

                        """,
                musicDetails.toString(), tracksList.size());

    }

}
