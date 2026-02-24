package com.sim.world.agent;

import com.sim.world.Coordinate;
import com.sim.world.World;

public record Position(float x, float y) {

    public Coordinate nearestCoordinate(World world) {
        int cX = Math.clamp(Math.round(x), 0, world.width()-1);
        int cY = Math.clamp(Math.round(y), 0, world.height()-1);
        return new Coordinate(cX, cY);
    }

}
