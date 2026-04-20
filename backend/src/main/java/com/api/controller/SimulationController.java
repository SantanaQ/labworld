package com.api.controller;

import com.api.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sim")
public class SimulationController {

    private static final double MIN_SPEED = 0.1;
    private static final double MAX_SPEED = 4.0;

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/start/{sessionId}")
    public ResponseEntity<Void> start(@PathVariable String sessionId) {
        simulationService.start(UUID.fromString(sessionId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pause/{sessionId}")
    public ResponseEntity<Void> pause(@PathVariable String sessionId) {
        simulationService.pause(UUID.fromString(sessionId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resume/{sessionId}")
    public ResponseEntity<Void> resume(@PathVariable String sessionId) {
        simulationService.resume(UUID.fromString(sessionId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop/{sessionId}")
    public ResponseEntity<Void> stop(@PathVariable String sessionId) {
        simulationService.stop(UUID.fromString(sessionId));
        return ResponseEntity.ok().build();
    }


    @PostMapping("/speed/{sessionId}/{speed}")
    public ResponseEntity<Void> speed(@PathVariable String sessionId, @PathVariable double speed) {
        speed = Math.clamp(speed, MIN_SPEED, MAX_SPEED);
        simulationService.applySpeed(UUID.fromString(sessionId), speed);
        return ResponseEntity.ok().build();
    }

}
