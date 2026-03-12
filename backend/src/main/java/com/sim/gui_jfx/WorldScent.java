package com.sim.gui_jfx;


import com.sim.snapshot.WorldSnapshot;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

public class WorldScent implements RenderLayer {

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Color baseColor = Color.BLACK;

    public WorldScent() {
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

        float val = worldSnap.scent()[y][x];
        gc.setFill(color(val));
        gc.fillRect(x * App.CELL_SIZE, y * App.CELL_SIZE, App.CELL_SIZE, App.CELL_SIZE);

    }

    private Color color(float value) {
        return Color.color(
                baseColor.getRed(),
                baseColor.getGreen(),
                baseColor.getBlue(),
                value);
    }
}

