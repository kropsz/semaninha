package com.kropsz.mslastfm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.mslastfm.dto.album.Album;
import com.kropsz.mslastfm.dto.album.AlbumsResponse;
import com.kropsz.mslastfm.dto.album.Artist;
import com.kropsz.mslastfm.dto.album.Image;
import com.kropsz.mslastfm.dto.album.TopAlbums;

@ExtendWith(MockitoExtension.class)
class CollageConstructorTest {
    
    @Mock
    private AlbumsResponse albumsResponse;

    @Mock
    private TopAlbums topAlbums;

    @Mock
    private Album album;

    @Mock
    private Artist artist;

    @Mock
    private Image image;

    @InjectMocks
    private CollageConstructor collageConstructor;

    @BeforeEach
    public void setUp() {
        when(albumsResponse.getTopalbums()).thenReturn(topAlbums);
        when(topAlbums.getAlbum()).thenReturn(Collections.singletonList(album));
        when(album.getName()).thenReturn("Album Name");
        when(album.getArtist()).thenReturn(artist);
        when(artist.getName()).thenReturn("Artist Name");
        when(album.getImage()).thenReturn(Collections.singletonList(image));
        when(image.getSize()).thenReturn("extralarge");
        when(image.getText()).thenReturn("http://image.url");
    }

    @Test
    @DisplayName("Should draw images in grid")
    void testDrawImagesInGrid() {
        BufferedImage result = collageConstructor.drawImagesInGrid(albumsResponse, 1);

        assertEquals(CollageConstructor.GRID_WIDTH, result.getWidth());
        assertEquals(CollageConstructor.GRID_HEIGHT, result.getHeight());
    }
}
