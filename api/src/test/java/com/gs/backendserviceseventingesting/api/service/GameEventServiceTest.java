package com.gs.backendserviceseventingesting.api.service;

import com.gs.backendserviceseventingesting.api.db.GameEventsRepository;
import com.gs.backendserviceseventingesting.api.model.Event;
import com.gs.backendserviceseventingesting.api.model.GameEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameEventServiceTest {

    @Mock
    private GameEventsRepository gameEventsRepository;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private GameEventService gameEventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getGameEventWhenIdDoesNotExist() {
        UUID eventId = UUID.randomUUID();

        Mockito.when(gameEventsRepository.findById(eventId)).thenReturn(Optional.empty());

        assertNull(gameEventService.getGameEvent(eventId));

        Mockito.verify(gameEventsRepository, Mockito.times(1)).findById(eventId);
    }

    @Test
    public void getGameEventWhenIdDoesExist() {
        UUID eventId = UUID.randomUUID();

        Event event = Event.builder()
                .player_id(UUID.randomUUID())
                .id(eventId)
                .event_time(1234)
                .event_code("code")
                .event_desc("desc")
                .build();

        Mockito.when(gameEventsRepository.findById(eventId)).thenReturn(Optional.of(event));

        GameEvent gameEvent = gameEventService.getGameEvent(eventId);

        assertNotNull(gameEvent);
        assertEquals(event.getId(), gameEvent.getEventId());
        assertEquals(event.getPlayer_id(), gameEvent.getPlayerId());
        assertEquals((Long)event.getEvent_time(), gameEvent.getTimestamp());
        assertEquals(event.getEvent_code(), gameEvent.getEventCode());
        assertEquals(event.getEvent_desc(), gameEvent.getEventDesc());

        Mockito.verify(gameEventsRepository, Mockito.times(1)).findById(eventId);
    }

    @Test
    public void queueGameEvent() {
        GameEvent gameEvent = GameEvent.builder()
                .playerId(UUID.randomUUID())
                .timestamp(1234L)
                .eventCode("code")
                .eventDesc("desc")
                .build();

        Mockito.doNothing().when(messageService).sendMessage(Mockito.any());

        assertNotNull(gameEventService.queueGameEvent(gameEvent));

        Mockito.verify(messageService).sendMessage(Mockito.any());
    }
}
