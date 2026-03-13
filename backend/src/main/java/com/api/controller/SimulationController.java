package com.api.controller;

import com.api.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sim")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start() {
        if(simulationService.hasConfig()) {
            simulationService.start();
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.badRequest().build();

    }

    @PostMapping("/pause")
    public ResponseEntity<Void> pause() {
        simulationService.pause();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resume")
    public ResponseEntity<Void> resume() {
        simulationService.resume();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop")
    public ResponseEntity<Void> stop() {
        simulationService.stop();
        return ResponseEntity.ok().build();
    }


    @PostMapping("/speed/{speed}")
    public ResponseEntity<Void> speed(@PathVariable double speed) {
        simulationService.setSpeed(speed);
        return ResponseEntity.ok().build();
    }
}
