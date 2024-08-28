package com.kropsz.mslastfm.service.lastfm.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.dto.UserTrackList;
import com.kropsz.mslastfm.dto.track.Track;
import com.kropsz.mslastfm.exception.RedisOperationException;
import com.kropsz.mslastfm.feign.LastfmClient;
import com.kropsz.mslastfm.publisher.handler.impl.DefaultTrackHandler;
import com.kropsz.mslastfm.service.lastfm.TrackService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackServiceImpl implements TrackService {

    private static final String RECENT_TRACKS_KEY = "recent_tracks";

    private final RedisTemplate<String, Object> redisTemplate;
    private final LastfmClient lastfmClient;
    private final ObjectMapper objectMapper;
    private final DefaultTrackHandler trackHandler;

    @Override
    public void getRecentTracks(Request request) {
        UserTrackList userTracks = getRecentTracksFromRedis(request.getUser());

        if (userTracks != null && userTracks.getPeriod().equals(request.getPeriod())){
            trackHandler.handleTracksFromRedis(userTracks);
            return;
        }

        List<Track> tracks = fetchAndUpdateTracks(request, userTracks != null ? userTracks.getTracks() : null);

        trackHandler.handleTracksFromApi(new UserTrackList(request.getUser(), tracks, request.getPeriod()));
    }

    @Override
    public List<Track> fetchAndUpdateTracks(Request request, List<Track> existingTracks) {
        var response = lastfmClient.getTopTracks(request);
        var newTracks = response.getToptracks().getTrack();

        Set<Track> newTrackSet = new HashSet<>(newTracks);

        if (existingTracks != null) {
            existingTracks.removeIf(track -> !newTrackSet.contains(track));
        } else {
            existingTracks = new ArrayList<>();
        }

        for (Track newTrack : newTracks) {
            if (!existingTracks.contains(newTrack)) {
                existingTracks.add(newTrack);
            }
        }

        saveOnRedis(existingTracks, request.getUser(), request.getPeriod());

        return existingTracks;
    }

    @Override
    public void saveOnRedis(List<Track> tracks, String user, String period) {
        try {
            String key = getRedisKey(user);
            UserTrackList userTracksWithPeriod = new UserTrackList(user, tracks, period);
            String jsonTracksWithPeriod = objectMapper.writeValueAsString(userTracksWithPeriod);
            redisTemplate.opsForValue().set(key, jsonTracksWithPeriod, 1, TimeUnit.HOURS);
        } catch (Exception e) {
            throw new RedisOperationException("Error saving recent tracks on Redis");
        }
    }

    @Override
    public UserTrackList getRecentTracksFromRedis(String user) {
        try {
            String key = getRedisKey(user);
            String jsonRecentTracks = (String) redisTemplate.opsForValue().get(key);
            if (jsonRecentTracks != null) {
                return objectMapper.readValue(jsonRecentTracks, UserTrackList.class);
            }
        } catch (Exception e) {
            throw new RedisOperationException("Error getting recent tracks from Redis");
        }
        return null;
    }

    private String getRedisKey(String user) {
        return RECENT_TRACKS_KEY + ":" + user;
    }

}