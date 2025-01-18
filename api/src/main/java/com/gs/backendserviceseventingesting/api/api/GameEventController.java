package com.gs.backendserviceseventingesting.api.api;

import com.gs.backendserviceseventingesting.api.service.GameEventService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameEventController {

    private final GameEventService gameEventService;

    public GameEventController(GameEventService gameEventService) {
        this.gameEventService = gameEventService;
    }


}
