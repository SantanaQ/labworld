package com.sim.world;

import com.sim.agent.Agent;
import com.sim.config.WorldConfig;

import java.util.ArrayList;
import java.util.List;

public class OccupancyGrid {

    private final List<Agent>[] cells;
    private final int width;
    private final int height;

    @SuppressWarnings("unchecked")
    public OccupancyGrid(int width, int height) {
        this.width = width;
        this.height = height;
        cells = (List<Agent>[]) new List[width * height];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new ArrayList<>();
        }
    }

    public void clear() {
        for (List<Agent> cell : cells) {
            cell.clear();
        }
    }

    public void set(Coordinate coordinate, Agent agent) {
        cells[coordinate.y() * width + coordinate.x()].add(agent);
    }

    public void refresh(World world, List<Agent> agents) {
        clear();
        for(Agent agent : agents) {
            Coordinate coordinate = agent.position().nearestCoordinate(world);
            set(coordinate, agent);
        }
    }

    public int agentCount(int windowSize, Coordinate origin) {
        int delta = windowSize / 2;
        int count = 0;
        int originX = origin.x();
        int originY = origin.y();
        for(int y = originY - delta; y <= originY + delta; y++) {
            for(int x = originX - delta; x <= originX + delta; x++) {
                if(isInBounds(x, y)) {
                    count += cells[y * width + x].size();
                }
            }
        }
        return count;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

}
