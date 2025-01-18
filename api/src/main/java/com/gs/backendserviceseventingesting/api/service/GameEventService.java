package com.gs.backendserviceseventingesting.api.service;

import com.gs.backendserviceseventingesting.api.model.GameEvent;

import java.util.UUID;

public class GameEventService {

    public UUID queueGameEvent(GameEvent gameEvent) {
        UUID eventID = UUID.randomUUID();
        gameEvent.setEventId(eventID);

        return eventID;
    }

    public GameEvent getGameEvent(UUID gameEventId) {
        return GameEvent.builder().build();
    }
}
