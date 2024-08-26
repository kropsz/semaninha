package com.kropsz.msspotify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kropsz.msspotify.entity.UserDetails;
import com.kropsz.msspotify.entity.enums.PlaylistType;
import com.kropsz.msspotify.service.PlaylistService;
import com.kropsz.msspotify.service.session.UserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final UserDetailsService userDetails;

    @PostMapping("/create/{user}")
    public ResponseEntity<String> getPlaylistLink(@PathVariable String user) {
        UserDetails details = userDetails.getUserDetails();
        return ResponseEntity.ok(playlistService.buildPlaylistWithTracks(details, user, PlaylistType.USER_TRACKS));
    }

    @PostMapping("/recommend/{user}")
    public ResponseEntity<String> getRecommendation(@PathVariable String user) throws JsonProcessingException {
        UserDetails details = userDetails.getUserDetails();
        return ResponseEntity.ok(playlistService.buildPlaylistWithTracks(details, user, PlaylistType.SUGGESTION_TRACKS));
    }

}
