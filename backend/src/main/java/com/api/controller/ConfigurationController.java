package com.api.controller;

import com.api.resource.JsonWorldConfig;
import com.api.service.SimulationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigurationController {

    private final SimulationService simulationService;

    public ConfigurationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping
    public void configure(@RequestBody JsonWorldConfig config) {
        simulationService.setConfig(config);
    }
}
