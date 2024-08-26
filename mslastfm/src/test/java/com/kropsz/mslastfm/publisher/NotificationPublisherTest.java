package com.kropsz.mslastfm.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.mslastfm.dto.UserTrackList;
import com.kropsz.mslastfm.dto.album.Artist;
import com.kropsz.mslastfm.dto.track.Track;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotificationPublisherTest {

    @InjectMocks
    private NotificationPublisher notificationPublisher;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;
    

    @Test
    void testNotifySuccessfullySendsMessage() throws Exception {
        UserTrackList payload = new UserTrackList("user123", List.of(new Track("testTrack", new Artist("testArtist"))),
                "7day");

        String jsonTracksWithPeriod = "{\"user\":\"user123\"," +
                "\"tracks\":[{\"name\":\"testTrack\",\"artist\":{\"name\":\"testArtist\"}}]," +
                "\"period\":\"7day\"}";

        String exchange = "exchange";
        ReflectionTestUtils.setField(notificationPublisher, "exchange", exchange);

        when(objectMapper.writeValueAsString(payload)).thenReturn(jsonTracksWithPeriod);

        notificationPublisher.notify(payload);

        verify(rabbitTemplate).convertAndSend(exchange, "", jsonTracksWithPeriod);
    }

    @Test
    @DisplayName("Should convert UserTrackList to JSON")
    void testConvertToJson() throws JsonProcessingException {
        UserTrackList payload = new UserTrackList("user123", List.of(new Track("testTrack", new Artist("testArtist"))),
                "7day");

        String jsonTracksWithPeriod = "{\"user\":\"user123\"," +
                "\"tracks\":[{\"name\":\"testTrack\",\"artist\":{\"name\":\"testArtist\"}}]," +
                "\"period\":\"7day\"}";

        when(objectMapper.writeValueAsString(payload)).thenReturn(jsonTracksWithPeriod);

        String result = notificationPublisher.convertToJson(payload);

        assertEquals(jsonTracksWithPeriod, result);
    }

}