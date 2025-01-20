package com.gs.backendserviceseventingesting.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class GameEvent {

    private UUID playerId;

    private UUID eventId;

    private Long timestamp;

    private String eventCode;

    private String eventDesc;
}
