package com.sim.snapshot;

import com.sim.world.Coordinate;
import com.sim.world.OccupancyGrid;

public class OccupancySnapshot {

    private final int[][] occupancy;

    public OccupancySnapshot(OccupancyGrid grid, int width, int height) {
        occupancy = new int[height][width];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Coordinate coord = new Coordinate(x, y);
                occupancy[y][x] = grid.at(coord).size();
            }
        }
    }

    public int[][] values() {
        return occupancy;
    }

}
