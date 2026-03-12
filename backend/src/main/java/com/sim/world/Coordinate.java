package com.sim.world;

import java.util.ArrayList;
import java.util.List;

public record Coordinate(int x, int y) {


    public Coordinate add(int dx, int dy) {
        return new Coordinate(x + dx, y + dy);
    }

    public Coordinate add(Coordinate other) {
        return new Coordinate(x + other.x, y + other.y);
    }





}
