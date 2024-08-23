package com.kropsz.mslastfm.data.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "lastfm_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    @Id
    private String id;
    private String user;
    private List<Collage> collages;

    public UserData(String user) {
        this.user = user;
        this.collages = new ArrayList<>();

    }

    public void addCollage(Collage collage) {
        this.collages.add(collage);
    }
}
