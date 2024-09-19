package com.kropsz.mslastfm.service.user.impl;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.data.repository.UserDataRepository;
import com.kropsz.mslastfm.dto.track.Track;
import com.kropsz.mslastfm.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDataRepository userRepository;

    @Override
    public UserData getUserData(String username) {
        return userRepository.findByUser(username)
                .orElseGet(() -> createNewUserData(username));
    }

    private UserData createNewUserData(String username) {
        return new UserData(username);
    }

    @Override
    public Collage addCollageToUser(UserData user, URL fileName, String period, List<Track> tracks){
        Collage collage = new Collage(fileName, LocalDate.now(), period, tracks);
        user.addCollage(collage);
        userRepository.save(user);
        return collage;
    }

    @Override
    public List<Collage> getCollages(String username) {
        var optionalUser = userRepository.findByUser(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getCollages();
        }
        return List.of();

    }

}