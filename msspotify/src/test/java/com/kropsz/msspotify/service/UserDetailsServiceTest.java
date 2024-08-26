package com.kropsz.msspotify.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kropsz.msspotify.entity.UserDetails;
import com.kropsz.msspotify.service.session.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsService();
    }

    @Test
    void testSaveUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setId("userId");
        userDetails.setAccessToken("accessToken");
        userDetails.setRefreshToken("refreshToken");

        userDetailsService.saveUserDetails(userDetails);

        assertEquals(userDetails, userDetailsService.getUserDetails());
    }
}