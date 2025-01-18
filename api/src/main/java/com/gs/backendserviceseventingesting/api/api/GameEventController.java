package com.gs.backendserviceseventingesting.api.api;


import com.gs.backendserviceseventingesting.api.model.GameEvent;
import com.gs.backendserviceseventingesting.api.model.GameEventReceipt;
import com.gs.backendserviceseventingesting.api.service.GameEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class GameEventController {

    private final GameEventService gameEventService;

    public GameEventController(GameEventService gameEventService) {
        this.gameEventService = gameEventService;
    }

    @PostMapping(
            value = "/event"
    )
    public ResponseEntity<GameEventReceipt> saveGameEvent(@RequestBody GameEvent gameEvent) {
        if (requestValid(gameEvent)) {
            return ResponseEntity
                    .status(202)
                    .body(GameEventReceipt
                            .builder()
                            .eventId(gameEventService.queueGameEvent(gameEvent))
                            .build()
                    );
        }

        return ResponseEntity
                .status(400)
                .build();
    }

    @GetMapping(
            value = "/event/{id}"
    )
    public ResponseEntity<GameEvent> getGameEvent(@PathVariable UUID id) {
        return ResponseEntity
                .status(200)
                .body(gameEventService.getGameEvent(id));
    }

    private boolean requestValid(GameEvent gameEvent) {
        if (gameEvent == null) {
            return false;
        }
        if (gameEvent.getPlayerId() == null) {
            return false;
        }
        if (gameEvent.getEventCode().isBlank()) {
            return false;
        }
        if (gameEvent.getEventDesc().isBlank()) {
            return false;
        }

        return true;
    }
}
