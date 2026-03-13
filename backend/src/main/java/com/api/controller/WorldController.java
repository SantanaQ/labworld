package com.api.controller;

import com.api.service.SimulationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/world")
public class WorldController {

    private final SimulationService simulationService;

    public WorldController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/start")
    public void start() {
        simulationService.start();
    }

    @PostMapping("/pause")
    public void pause() {
        simulationService.pause();
    }

    @PostMapping("/speed/{speed}")
    public void speed(@PathVariable double speed) {
        simulationService.setSpeed(speed);
    }
}
