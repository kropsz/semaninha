package com.kropsz.msspotify.controller;

import java.net.URI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.msspotify.config.SpotifyConfig;
import com.kropsz.msspotify.entity.UserDetails;
import com.kropsz.msspotify.service.session.UserDetailsService;

import lombok.RequiredArgsConstructor;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final SpotifyConfig spotifyConfig;
    private final UserDetailsService userDetailsService;

    @GetMapping("/login")
    public String spotifyLogin() {

        SpotifyApi spotifyApi = spotifyConfig.getSpotifyObject();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("playlist-modify-public, playlist-modify-private, user-read-private, user-read-email")
                .show_dialog(true)
                .build();

        final URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();
    }

    @GetMapping("/get-user-code")
    public UserDetails getSpotifyUserCode(@RequestParam String code) throws SpotifyWebApiException {
        SpotifyApi spotifyApi = spotifyConfig.getSpotifyObject();

        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
        try {
            final AuthorizationCodeCredentials authorizationCode = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCode.getAccessToken());

            final GetCurrentUsersProfileRequest getCurrentUsersProfile = spotifyApi.getCurrentUsersProfile().build();

            User user = getCurrentUsersProfile.execute();
            UserDetails userDetails = new UserDetails(
                    user.getId(),
                    authorizationCode.getAccessToken(),
                    authorizationCode.getRefreshToken());
            userDetailsService.saveUserDetails(userDetails);
            return userDetails;
        } catch (Exception e) {
            throw new SpotifyWebApiException("Error while getting user code");
        }
    }

}