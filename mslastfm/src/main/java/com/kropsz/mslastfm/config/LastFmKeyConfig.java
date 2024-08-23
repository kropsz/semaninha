package com.kropsz.mslastfm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class LastFmKeyConfig {

    @Value("${lastfm.api.key}")
    private String lastFmApiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.query("api_key", lastFmApiKey);
    }
}