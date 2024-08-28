package com.kropsz.mslastfm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.dto.LinkCollage;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.dto.album.AlbumsResponse;
import com.kropsz.mslastfm.feign.LastfmClient;
import com.kropsz.mslastfm.service.lastfm.impl.CollageServiceImpl;
import com.kropsz.mslastfm.service.s3.S3Service;
import com.kropsz.mslastfm.service.user.UserService;
import com.kropsz.mslastfm.util.CollageConstructor;

@ExtendWith(MockitoExtension.class)
class CollageServiceTest {

    @InjectMocks
    private CollageServiceImpl collageService;

    @Mock
    private LastfmClient lastfmClient;

    @Mock
    private CollageConstructor collageConstructor;

    @Mock
    private UserService userService;

    @Mock
    private S3Service s3Service;

    private Request request;
    private UserData user;
    private BufferedImage image;
    private AlbumsResponse albumsResponse;

    @BeforeEach
    void setUp() {
        request = new Request("testUser", "7day", 10);
        user = new UserData("testUser");
        image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        albumsResponse = new AlbumsResponse();
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Create collage")
    void testCreateCollage() throws IOException {
        
        Collage collage = new Collage(new URL("http://example.com/collage_testUser_0.png"), LocalDate.now());

        when(userService.getUserData(request.getUser())).thenReturn(user);
        when(lastfmClient.getTopAlbums(request)).thenReturn(albumsResponse);
        when(collageConstructor.drawImagesInGrid(albumsResponse, request.getLimit())).thenReturn(image);
        when(s3Service.getPublicUrl(anyString())).thenReturn(new URL("http://example.com/collage_testUser_0.png"));
        when(collageService.saveCollage(image, user)).thenReturn(collage);

        LinkCollage link = collageService.createCollage(request);

        verify(userService).getUserData(request.getUser());
        verify(lastfmClient).getTopAlbums(request);
        verify(collageConstructor).drawImagesInGrid(albumsResponse, request.getLimit());
        assertEquals(link.getLink(), collage.getImageUrl().toString());

    }


    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Save collage")
    void testSaveCollage() throws IOException {
        String fileName = "collage_testUser_0.png";
        when(s3Service.getPublicUrl(fileName)).thenReturn(new URL("http://example.com/" + fileName));

        collageService.saveCollage(image, user);

        ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(s3Service).uploadImage(eq(image), fileNameCaptor.capture());
        assertEquals(fileName, fileNameCaptor.getValue());

        verify(userService).addCollageToUser(eq(user), any(URL.class));
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Get collage URL")
    void testGetCollageUrl() throws MalformedURLException {
        String fileName = "collage_testUser_0.png";
        URL expectedUrl = new URL("http://example.com/" + fileName);
        when(s3Service.getPublicUrl(fileName)).thenReturn(expectedUrl);

        URL actualUrl = collageService.getCollageUrl(fileName);

        assertEquals(expectedUrl, actualUrl);
    }
}