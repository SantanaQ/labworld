package com.api.controller;

import com.api.dto.AgentDTO;
import com.api.resource.response.AgentInfoResponse;
import com.api.service.SimulationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/sim/agents")
public class AgentController {

    private final SimulationService simulationService;

    public AgentController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/info/{sessionId}/{agentId}")
    public ResponseEntity<AgentInfoResponse> agentInfo(@PathVariable String sessionId, @PathVariable String agentId) {
        try {
            short id = Short.parseShort(agentId);
            AgentDTO found = simulationService.findAgentById(UUID.fromString(sessionId), id);
            AgentInfoResponse agentInfoResponse = new AgentInfoResponse(
                    found.id(),
                    found.position().x(),
                    found.position().y(),
                    found.velocity().vx(),
                    found.velocity().vy(),
                    found.speed(),
                    found.needs().energy(),
                    found.needs().hunger(),
                    found.needs().heat(),
                    found.needs().curiosity(),
                    found.needs().fear()
            );
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(agentInfoResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }
}
