package com.api.dto;

import com.sim.agent.Needs;
import com.sim.agent.Position;
import com.sim.agent.Vector;

public record AgentDTO(short id, Position position, Vector velocity, float speed,  Needs needs) {
}
