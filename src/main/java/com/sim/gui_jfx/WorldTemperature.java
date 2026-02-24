package com.sim.gui_jfx;

import com.sim.snapshot.WorldSnapshot;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WorldTemperature implements RenderLayer{

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Color[] colorMap = new Color[256];

    public WorldTemperature() {
        this.canvas = new Canvas();
        this.gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < 256; i++) {
            float t = i / 255f;
            colorMap[i] = turboColor(t, 0f, 1f);
        }
    }

    @Override
    public Canvas canvas() {
        return canvas;
    }

    @Override
    public GraphicsContext gc() {
        return gc;
    }


    @Override
    public void renderCell(WorldSnapshot worldSnap, int x, int y) {
        float temp = worldSnap.temperature()[x][y];
        int idx = Math.max(0, Math.min(255, Math.round(temp * 255)));
        gc.setFill(colorMap[idx]);
        gc.fillRect(
                x * App.CELL_SIZE,
                y * App.CELL_SIZE,
                App.CELL_SIZE,
                App.CELL_SIZE
        );
    }

    private Color turboColor(float val, float min, float max) {
        float t = (val - min) / (max - min);
        t = Math.max(0f, Math.min(1f, t));

        float r = 34.61f
                + t * (1172.33f
                + t * (-10793.56f
                + t * (33300.12f
                + t * (-38394.49f
                + t * 14825.05f))));

        float g = 23.31f
                + t * (557.33f
                + t * (1225.33f
                + t * (-3574.96f
                + t * (1445.14f))));

        float b = 27.2f
                + t * (3211.1f
                + t * (-15327.97f
                + t * (27814.0f
                + t * (-22569.18f
                + t * 6838.66f))));

        return Color.rgb(
                clamp255(r),
                clamp255(g),
                clamp255(b),
                0.7f
        );
    }

    private int clamp255(float x) {
        return Math.max(0, Math.min(255, Math.round(x)));
    }



}
