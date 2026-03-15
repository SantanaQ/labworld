package com.sim.gui_jfx;

import com.sim.snapshot.WorldSnapshot;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WorldAgents implements RenderLayer {

    private final Canvas canvas;
    private final GraphicsContext gc;

    public WorldAgents() {
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
    }

    public void renderAll(WorldSnapshot worldSnap) {
        /*for(var agent : worldSnap.agents()) {
            gc.setFill(Color.BLACK);
            float fx = agent.position().x();
            float fy = agent.position().y();
            gc.fillOval(fx * App.CELL_SIZE -(double) 1/2 * App.CELL_SIZE,
                    fy * App.CELL_SIZE - (double) 1/2 * App.CELL_SIZE,
                    App.CELL_SIZE*2,
                    App.CELL_SIZE*2);
            gc.setFill(Color.WHITE);
            gc.fillOval(fx * App.CELL_SIZE,
                    fy * App.CELL_SIZE,
                    App.CELL_SIZE,
                    App.CELL_SIZE);
        }*/
    }

}
