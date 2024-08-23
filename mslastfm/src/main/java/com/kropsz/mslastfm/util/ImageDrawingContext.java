package com.kropsz.mslastfm.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDrawingContext{
    String imageUrl;
    String albumName;
    String artistName;
    int x;
    int y;
    int imgWidth;
    int imgHeight;
}