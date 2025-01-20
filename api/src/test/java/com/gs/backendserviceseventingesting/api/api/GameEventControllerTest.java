package com.gs.backendserviceseventingesting.api.api;

import com.gs.backendserviceseventingesting.api.model.GameEvent;
import com.gs.backendserviceseventingesting.api.service.GameEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GameEventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GameEventService gameEventService;

    @InjectMocks
    private GameEventController gameEventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gameEventController).build();
    }

    @Test
    public void postEventWithEmptyRequestBody() throws Exception {
        mockMvc.perform(
                post("/event")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();

        Mockito.verifyNoInteractions(gameEventService);
    }

    @Test
    public void postEventWithInvalidRequestBody() throws Exception {
        UUID playerId = UUID.randomUUID();

        String requestBody = "{}";

        // Empty request body
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        // Missing playerId request body
        requestBody = "{" +
                "\"timestamp\": 1234," +
                "\"eventCode\": \"event-code\"," +
                "\"eventDesc\": \"event-desc\"" +
                "}";
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        // Empty playerId request body
        requestBody = "{" +
                "\"playerId\": \"\"," +
                "\"timestamp\": 1234," +
                "\"eventCode\": \"event-code\"," +
                "\"eventDesc\": \"event-desc\"" +
                "}";
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        // Missing timestamp request body
        requestBody = "{" +
                "\"playerId\": \"" + playerId + "\"," +
                "\"eventCode\": \"event-code\"," +
                "\"eventDesc\": \"event-desc\"" +
                "}";
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        // Empty timestamp request body
        requestBody = "{" +
                "\"playerId\": \"\"," +
                "\"timestamp\": null," +
                "\"eventCode\": \"event-code\"," +
                "\"eventDesc\": \"event-desc\"" +
                "}";
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        // Missing event code request body
        requestBody = "{" +
                "\"playerId\": \"" + playerId + "\"," +
                "\"timestamp\": 1234," +
                "\"eventDesc\": \"event-desc\"" +
                "}";
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        // Empty event code request body
        requestBody = "{" +
                "\"playerId\": \"" + playerId + "\"," +
                "\"timestamp\": 1234," +
                "\"eventCode\": null," +
                "\"eventDesc\": \"event-desc\"" +
                "}";
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        // Missing event desc request body
        requestBody = "{" +
                "\"playerId\": \"" + playerId + "\"," +
                "\"timestamp\": 1234," +
                "\"eventCode\": \"event-code\"," +
                "}";
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        // Empty event desc request body
        requestBody = "{" +
                "\"playerId\": \"" + playerId + "\"," +
                "\"timestamp\": 1234," +
                "\"eventCode\": \"event-code\"," +
                "\"eventDesc\": null" +
                "}";
        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(400))
                .andReturn();

        Mockito.verifyNoInteractions(gameEventService);
    }

    @Test
    public void postEventWithValidRequestBody() throws Exception {
        UUID playerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        String requestBody = "{" +
                "\"playerId\": \"" + playerId + "\"," +
                "\"timestamp\": 1234," +
                "\"eventCode\": \"event-code\"," +
                "\"eventDesc\": \"event-desc\"" +
                "}";


        GameEvent event = GameEvent.builder()
                .playerId(playerId)
                .eventId(null)
                .timestamp(1234L)
                .eventCode("event-code")
                .eventDesc("event-desc")
                .build();

        Mockito.when(gameEventService.queueGameEvent(event)).thenReturn(eventId);

        mockMvc.perform(
                        post("/event")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().is(202))
                .andReturn();

        Mockito.verify(gameEventService).queueGameEvent(event);
    }

    @Test
    public void getEventWithIdNotFound() throws Exception {
        UUID eventId = UUID.randomUUID();

        Mockito.when(gameEventService.getGameEvent(eventId)).thenReturn(null);

        mockMvc.perform(get("/event/" + eventId))
                .andExpect(status().is(404))
                .andReturn();

        Mockito.verify(gameEventService).getGameEvent(eventId);
    }

    @Test
    public void getEventWithIdFound() throws Exception {
        UUID eventId = UUID.randomUUID();

        GameEvent event = GameEvent.builder()
                .playerId(UUID.randomUUID())
                .eventId(eventId)
                .timestamp(1234L)
                .eventCode("event-code")
                .eventDesc("event-desc")
                .build();

        Mockito.when(gameEventService.getGameEvent(eventId)).thenReturn(event);

        MvcResult result = mockMvc.perform(get("/event/" + eventId))
                .andExpect(status().is(200))
                .andReturn();

        assertTrue(
            result.getResponse().getContentAsString()
            .contains(
                    "\"playerId\":\"" + event.getPlayerId() + "\"," +
                    "\"eventId\":\"" + event.getEventId() + "\""
            )
        );

        Mockito.verify(gameEventService).getGameEvent(eventId);
    }
}