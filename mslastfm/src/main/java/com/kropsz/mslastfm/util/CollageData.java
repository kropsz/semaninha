package com.kropsz.mslastfm.util;

import java.util.ArrayList;
import java.util.List;

import com.kropsz.mslastfm.dto.album.Album;
import com.kropsz.mslastfm.dto.album.AlbumsResponse;
import com.kropsz.mslastfm.dto.album.Image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollageData {

    private List<String> imageUrls = new ArrayList<>();
    private List<String> albumNames = new ArrayList<>();
    private List<String> artistNames = new ArrayList<>();

    public CollageData buildCollageData(AlbumsResponse response) {
        for (Album album : response.getTopalbums().getAlbum()) {
            albumNames.add(album.getName());
            artistNames.add(album.getArtist().getName());
            
            String imageUrl = album.getImage().stream()
                    .filter(image -> "extralarge".equals(image.getSize()))
                    .findFirst()
                    .map(Image::getText)
                    .orElse("");
            imageUrls.add(imageUrl);
        }

        return this;
    }
}