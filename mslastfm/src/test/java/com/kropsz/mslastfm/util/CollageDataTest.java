package com.kropsz.mslastfm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.mslastfm.dto.album.Album;
import com.kropsz.mslastfm.dto.album.AlbumsResponse;
import com.kropsz.mslastfm.dto.album.Artist;
import com.kropsz.mslastfm.dto.album.Image;
import com.kropsz.mslastfm.dto.album.TopAlbums;

@ExtendWith(MockitoExtension.class)
class CollageDataTest {

    private CollageData collageData;

    @BeforeEach
    void setUp() {
        collageData = new CollageData();
    }

    @Test
    void testBuildCollageData() {
        AlbumsResponse response = mock(AlbumsResponse.class);
        TopAlbums topAlbums = mock(TopAlbums.class);
        List<Album> albumList = new ArrayList<>();
        
        Album album = mock(Album.class);
        when(album.getName()).thenReturn("Album Name");
        Artist artist = mock(Artist.class);
        when(artist.getName()).thenReturn("Artist Name");
        when(album.getArtist()).thenReturn(artist);
        
        Image image = mock(Image.class);
        when(image.getSize()).thenReturn("extralarge");
        when(image.getText()).thenReturn("http://example.com/image.jpg");
        List<Image> images = new ArrayList<>();
        images.add(image);
        when(album.getImage()).thenReturn(images);
        
        albumList.add(album);
        when(topAlbums.getAlbum()).thenReturn(albumList);
        when(response.getTopalbums()).thenReturn(topAlbums);
        
        collageData.buildCollageData(response);
        
        assertEquals(1, collageData.getAlbumNames().size());
        assertEquals("Album Name", collageData.getAlbumNames().get(0));
        assertEquals(1, collageData.getArtistNames().size());
        assertEquals("Artist Name", collageData.getArtistNames().get(0));
        assertEquals(1, collageData.getImageUrls().size());
        assertEquals("http://example.com/image.jpg", collageData.getImageUrls().get(0));
    }
}