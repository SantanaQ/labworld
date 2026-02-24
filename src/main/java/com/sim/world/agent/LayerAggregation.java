package com.sim.world.agent;

import com.sim.layers.WorldLayer;
import com.sim.world.Coordinate;
import com.sim.world.World;

import java.util.List;

public class LayerAggregation {

    public static float mean(World world, Coordinate origin, WorldLayer source, int windowSize) {
        List<Coordinate> neighbors = world.neighbors(origin, windowSize);
        float total = 0;
        for(Coordinate neighbor : neighbors) {
            total += source.accessableAt(neighbor);
        }
        return total / neighbors.size();
    }

    public static float sum(World world, Coordinate origin, WorldLayer source, int windowSize) {
        List<Coordinate> neighbors = world.neighbors(origin, windowSize);
        float total = 0;
        for(Coordinate neighbor : neighbors) {
            total += source.accessableAt(neighbor);
        }
        return total;
    }

    public static float min(World world, Coordinate origin, WorldLayer source, int windowSize) {
        List<Coordinate> neighbors = world.neighbors(origin, windowSize);
        float min = source.accessableAt(origin);
        for(Coordinate neighbor : neighbors) {
            min = Math.min(min, source.accessableAt(neighbor));
        }
        return min;
    }

    public static float max(World world, Coordinate origin, WorldLayer source, int windowSize) {
        List<Coordinate> neighbors = world.neighbors(origin, windowSize);
        float max = source.accessableAt(origin);
        for(Coordinate neighbor : neighbors) {
            max = Math.max(max, source.accessableAt(neighbor));
        }
        return max;
    }

    public static Coordinate weightedCenter(World world, Coordinate origin, WorldLayer source, int windowSize) {
        List<Coordinate> neighbors = world.neighbors(origin, windowSize);
        float sum = sum(world, origin, source, windowSize);
        float prodX = 0;
        float prodY = 0;
        for (Coordinate neighbor : neighbors) {
            prodX += neighbor.x() * source.accessableAt(neighbor);
            prodY += neighbor.y() * source.accessableAt(neighbor);
        }
        int cX = (int) ((int) prodX / sum);
        int cY = (int) ((int) prodY / sum);
        return new Coordinate(cX, cY);
    }

    public static Coordinate bestFittingCenter(World world, Coordinate origin, WorldLayer source, int windowSize, int regionSize, float targetValue) {
        List<Coordinate> neighbors = world.neighbors(origin, windowSize);
        Coordinate best = neighbors.getFirst();
        float deltaOfCurrBest = Math.abs(mean(world, best, source, regionSize) - targetValue);
        for(Coordinate neighbor : neighbors) {
            float deltaOfNeighbor = Math.abs(mean(world, neighbor, source, regionSize) - targetValue);
            if(deltaOfNeighbor < deltaOfCurrBest) {
                best = neighbor;
                deltaOfCurrBest = deltaOfNeighbor;
            }
        }
        return best;
    }

}
