package com.api.controller;

import com.api.resource.JsonWorldConfig;
import com.api.service.SimulationService;
import com.sim.config.WorldConfig;
import com.sim.snapshot.WorldSnapshot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sim/config")
public class ConfigurationController {

    private final SimulationService simulationService;

    public ConfigurationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/load")
    public ResponseEntity<WorldConfig> configure(@RequestBody String json) {
        System.out.println(json);
        JsonWorldConfig jsonCfg = JsonWorldConfig.fromJson(json);
        simulationService.setConfig(jsonCfg);

        return ResponseEntity.ok(simulationService.config());
    }

    @PostMapping("/load-default")
    public ResponseEntity<WorldConfig> loadDefault() {
        simulationService.setConfig(new WorldConfig());

        return ResponseEntity.ok(simulationService.config());
    }

}
