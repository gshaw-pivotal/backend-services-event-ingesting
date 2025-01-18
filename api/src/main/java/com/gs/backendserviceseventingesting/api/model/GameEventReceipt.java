package com.gs.backendserviceseventingesting.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class GameEventReceipt {

    private UUID eventId;
}
