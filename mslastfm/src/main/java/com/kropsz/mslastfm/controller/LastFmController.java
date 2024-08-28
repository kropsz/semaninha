package com.kropsz.mslastfm.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.dto.LinkCollage;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.dto.track.Track;
import com.kropsz.mslastfm.service.lastfm.CollageService;
import com.kropsz.mslastfm.service.lastfm.TrackService;
import com.kropsz.mslastfm.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/semaninha")
@RequiredArgsConstructor
public class LastFmController {

    private final CollageService collageService;
    private final UserService userService;
    private final TrackService trackService;

    @PostMapping("/collage")
    public ResponseEntity<LinkCollage> createCollage(@RequestBody @Valid Request request) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(collageService.createCollage(request));
    }

    @GetMapping("/collage/{username}")
    public ResponseEntity<List<Collage>> getCollageByUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getCollages(username));
    }

    @GetMapping("/tracks")
    public ResponseEntity<List<Track>> getRecentTracks(@RequestBody @Valid Request request) {
        return ResponseEntity.ok().body(trackService.getRecentTracks(request));
    }

}