package com.sim.signal;

public class HashNoise {

    private final int seed;

    public HashNoise(int seed) {
        this.seed = seed;
    }

    public float random(int x, int y) {
        int h = x * 374761393
                + y * 668265263
                + seed * 31;

        h = (h ^ (h >> 13)) * 1274126177;
        return (h & 0x7fffffff) * 4.6566129e-10f;
    }
}
