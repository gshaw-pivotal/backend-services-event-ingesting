package com.gs.backendserviceseventingesting.api.service;

import com.gs.backendserviceseventingesting.api.db.GameEventsRepository;
import com.gs.backendserviceseventingesting.api.model.Event;
import com.gs.backendserviceseventingesting.api.model.GameEvent;

import java.util.Optional;
import java.util.UUID;

public class GameEventService {

    private final GameEventsRepository gameEventsRepository;

    public GameEventService(GameEventsRepository eventsRepository) {
        this.gameEventsRepository = eventsRepository;
    }

    public UUID queueGameEvent(GameEvent gameEvent) {
        UUID eventID = UUID.randomUUID();
        gameEvent.setEventId(eventID);

        return eventID;
    }

    public GameEvent getGameEvent(UUID gameEventId) {
        Optional<Event> event = gameEventsRepository.findById(gameEventId);

        if (event.isPresent()) {
            return createGameEvent(event.get());
        }

        return null;
    }

    private GameEvent createGameEvent(Event event) {
        return GameEvent.builder()
                .eventId(event.getId())
                .playerId(event.getPlayer_id())
                .timestamp(event.getEvent_time())
                .eventCode(event.getEvent_code())
                .eventDesc(event.getEvent_desc())
                .build();
    }
}
