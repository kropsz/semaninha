package com.kropsz.msspotify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

import java.net.URI;

@Configuration
public class SpotifyConfig {

    @Value("${spotify.api.clientId}")
    private String clientId;
    @Value("${spotify.api.clientSecret}")
    private String clientSecret;


    public SpotifyApi getSpotifyObject(){
        URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8082/api/get-user-code");

        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();
    }

}