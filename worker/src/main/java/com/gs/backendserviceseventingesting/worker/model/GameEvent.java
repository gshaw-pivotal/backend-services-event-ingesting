package com.gs.backendserviceseventingesting.worker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GameEvent {

    private UUID playerId;

    private UUID eventId;

    private long timestamp;

    private String eventCode;

    private String eventDesc;
}
