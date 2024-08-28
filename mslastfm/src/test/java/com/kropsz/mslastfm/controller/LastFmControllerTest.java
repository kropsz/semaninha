package com.kropsz.mslastfm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.mslastfm.dto.LinkCollage;
import com.kropsz.mslastfm.dto.Request;
import com.kropsz.mslastfm.service.lastfm.CollageService;
import com.kropsz.mslastfm.service.lastfm.TrackService;
import com.kropsz.mslastfm.service.user.UserService;

@WebMvcTest(LastFmController.class)
class LastFmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private TrackService trackService;

    @MockBean
    private CollageService collageService;

    @Test
    @DisplayName("Should create a collage link and return a 201 created status with expected LinkCollage")
    void testCreateLinkCollage() throws Exception {
        Request request = new Request("user", "7day", 20);

        String requestJson = objectMapper.writeValueAsString(request);
        LinkCollage link = new LinkCollage("http://exampla/photo");

        when(collageService.createCollage(request)).thenReturn(link);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/semaninha/collage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.link").value(link.getLink()));
    }

    @Test
    @DisplayName("Should throw a 404 not found status when user is not found")
    void testCreateLinkCollageUserNotFound() throws Exception {
        Request request = new Request("user", "7day", 20);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/semaninha/collage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should throw a 422 unprocessable entity status when period is invalid")
    void testCreateLinkCollageInvalidPeriod() throws Exception {
        Request request = new Request("user", "7930219ays", 20);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/semaninha/collage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should throw a 500 internal server error status when an unexpected error occurs")
    void testCreateLinkCollageUnexpectedError() throws Exception {

        Request request = new Request("user", "7day", 20);
        String requestJson = objectMapper.writeValueAsString(request);

        when(collageService.createCollage(any(Request.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/semaninha/collage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should return a 200 OK status with an empty list of collages by user")
    void testGetCollageByUser() throws Exception {
        String user = "user";

        when(userService.getCollages(user)).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/semaninha/collage/" + user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return a list of tracks by user")
    void testGetTracksByUser() throws Exception {
        Request request = new Request("user", "7day", 20);
        String requestJson = objectMapper.writeValueAsString(request);

        when(trackService.getRecentTracks(request)).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/semaninha/tracks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
