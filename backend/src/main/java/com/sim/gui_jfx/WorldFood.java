package com.sim.gui_jfx;

import com.sim.snapshot.WorldSnapshot;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

public class WorldFood implements RenderLayer {

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Color baseColor = Color.MIDNIGHTBLUE;

    public WorldFood() {
        this.canvas = new Canvas();
        this.gc = canvas.getGraphicsContext2D();
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

        float val = worldSnap.food()[y][x];
        gc.setFill(color(val));
        gc.fillOval(x * App.CELL_SIZE, y * App.CELL_SIZE, App.CELL_SIZE * val, App.CELL_SIZE * val);

    }

    private Color color(float value) {
        if(value == 0) return Color.color(
                baseColor.getRed(),
                baseColor.getGreen(),
                baseColor.getBlue(),
                0);
        return Color.color(
                baseColor.getRed(),
                baseColor.getGreen(),
                baseColor.getBlue(),
                1);
    }
}
