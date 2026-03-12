package com.sim.signal;

public class GridSignal implements SignalSource {

    private final float[][] grid;

    public GridSignal(float[][] grid) {
        this.grid = grid;
    }

    @Override
    public float sample(float x, float y) {
        int ix = Math.round(x);
        int iy = Math.round(y);

        if(ix < 0 || iy < 0 || iy >= grid.length || ix >= grid[0].length) {
            return 0;
        }

        return grid[iy][ix];
    }
}
