package com.kropsz.msspotify.service.session;

import com.kropsz.msspotify.entity.UserDetails;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UserDetailsService {

    private UserDetails userDetails;

    public synchronized void saveUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}