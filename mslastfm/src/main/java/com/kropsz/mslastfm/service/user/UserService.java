package com.kropsz.mslastfm.service.user;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kropsz.mslastfm.data.model.Collage;
import com.kropsz.mslastfm.data.model.UserData;
import com.kropsz.mslastfm.data.repository.UserDataRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDataRepository userRepository;

    public UserData getUserData(String username) {
        return userRepository.findByUser(username)
                .orElseGet(() -> createNewUserData(username));
    }

    private UserData createNewUserData(String username) {
        return new UserData(username);
    }

    public Collage addCollageToUser(UserData user, URL fileName) {
        Collage collage = new Collage(fileName, LocalDate.now());
        user.addCollage(collage);
        userRepository.save(user);
        return collage;
    }

    public List<Collage> getCollages(String username) {
        var optionalUser = userRepository.findByUser(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getCollages();
        }
        return List.of();

    }

}