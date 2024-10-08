package com.kropsz.mslastfm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.data.repository.UserDataRepository;
import com.kropsz.mslastfm.dto.album.Artist;
import com.kropsz.mslastfm.dto.track.Track;
import com.kropsz.mslastfm.service.user.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDataRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private List<Track> tracks;

    @BeforeEach
    void setUp() {
        tracks = new ArrayList<>(Arrays.asList(new Track("testTrack", new Artist("testArtist")),
                new Track("testTrack2", new Artist("testArtist2"))));
    }

    @Test
    @DisplayName("Should return existing user data")
    void testGetUserDataExistingUser() {
        String username = "existingUser";
        UserData userData = new UserData(username);
        when(userRepository.findByUser(username)).thenReturn(Optional.of(userData));

        UserData result = userService.getUserData(username);

        assertEquals(userData, result);
        verify(userRepository, times(1)).findByUser(username);
    }

    @Test
    @DisplayName("Should create new user data if user does not exist")
    void testGetUserDataNewUser() {
        String username = "newUser";
        when(userRepository.findByUser(username)).thenReturn(Optional.empty());

        UserData result = userService.getUserData(username);

        assertEquals(username, result.getUser());
        verify(userRepository, times(1)).findByUser(username);
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Should add collage to user and save")
    void testAddCollageToUser() throws MalformedURLException {
        UserData user = new UserData("user");
        URL fileName = new URL("http://example.com/collage.jpg");
        String period = "period";
        Collage collage = new Collage(fileName, LocalDate.now(), period, tracks);

        userService.addCollageToUser(user, fileName, period, tracks);

        assertEquals(1, user.getCollages().size());
        assertEquals(collage, user.getCollages().getFirst());
        verify(userRepository, times(1)).save(user);
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Should return user collages")
    void testGetCollagesByUser() throws MalformedURLException {
        String username = "user";
        UserData user = new UserData(username);
        String period = "period";
        user.addCollage(new Collage(new URL("http://example.com/collage.jpg"), LocalDate.now(), period, tracks));
        when(userRepository.findByUser(username)).thenReturn(Optional.of(user));

        var result = userService.getCollages(username);

        assertEquals(user.getCollages(), result);
        verify(userRepository, times(1)).findByUser(username);
    }

    @Test
    @DisplayName("Should return empty list if user does not exist")
    void testGetCollagesByUserNoUser() {
        String username = "user";
        when(userRepository.findByUser(username)).thenReturn(Optional.empty());

        var result = userService.getCollages(username);

        assertEquals(0, result.size());
        verify(userRepository, times(1)).findByUser(username);
    }

}