package com.sim.signal;

public class SignalField {

    private final int width;
    private final int height;
    private final float[] values;

    public SignalField(int width, int height, SignalSource source) {
        this.width = width;
        this.height = height;
        this.values = new float[width * height];

        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                values[i++] = source.sample(x, y);
            }
        }
    }

    public float sample(int x, int y) {
        if (x < 0) x = 0;
        else if (x >= width) x = width - 1;

        if (y < 0) y = 0;
        else if (y >= height) y = height - 1;

        return values[y * width + x];
    }
}
