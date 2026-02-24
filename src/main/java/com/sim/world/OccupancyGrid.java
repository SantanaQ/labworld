package com.sim.world;

import com.sim.world.agent.Agent;

import java.util.ArrayList;
import java.util.List;

public class OccupancyGrid {

    private final List<Agent>[] cells;
    private final int width;

    @SuppressWarnings("unchecked")
    public OccupancyGrid(int width, int height) {
        this.width = width;
        cells = (List<Agent>[]) new List[width * height];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new ArrayList<>();
        }
    }

    public List<Agent> at(Coordinate coordinate) {
        return cells[coordinate.y() * width + coordinate.x()];
    }

    public void clear() {
        for (List<Agent> cell : cells) {
            cell.clear();
        }
    }

    public void set(Coordinate coordinate, Agent agent) {
        cells[coordinate.y() * width + coordinate.x()].add(agent);
    }

    public List<Agent>[] cells() {
        return cells;
    }



}
