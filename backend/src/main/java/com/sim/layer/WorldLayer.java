package com.sim.layer;

public interface WorldLayer {
    int width();
    int height();

    float valueAt(int x, int y);
    float[] values();

    default void printValues() {
        for(int y = 0 ; y < height() ; y++) {
            for(int x = 0 ; x < width() ; x++) {
                System.out.print(valueAt(x, y) + " ");
            }
            System.out.println();
        }
    }

}
