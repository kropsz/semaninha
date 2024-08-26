package com.kropsz.msspotify.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Tracks {
    private String name;
    private Artist artist;

}