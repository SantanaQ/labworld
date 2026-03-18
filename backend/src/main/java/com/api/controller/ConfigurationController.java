package com.api.controller;

import com.api.resource.*;
import com.api.resource.response.SimulationInitResponse;
import com.api.service.SimulationService;
import com.sim.config.WorldConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sim/config")
public class ConfigurationController {

    private final SimulationService simulationService;

    public ConfigurationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/load")
    public ResponseEntity<SimulationInitResponse> configure(@RequestBody EditorConfig config) {

        EditorGraph graph = new EditorGraph(config);
        NodeGraphResolver resolver = new NodeGraphResolver(graph);
        WorldConfig worldConfig = resolver.configFromGraph();

        UUID sessionId = simulationService.createSession(worldConfig);

        SimulationInitResponse response =
                new SimulationInitResponse(sessionId, worldConfig);

        return ResponseEntity.ok(response);
    }


}
