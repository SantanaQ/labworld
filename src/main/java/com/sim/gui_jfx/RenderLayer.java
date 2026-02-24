package com.sim.gui_jfx;

import com.sim.snapshot.WorldSnapshot;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public interface RenderLayer {

    Canvas canvas();
    GraphicsContext gc();

    void renderCell(WorldSnapshot worldSnap, int x, int y);

    default void clear() {
        gc().clearRect(0, 0, canvas().getWidth(), canvas().getHeight());
    }

}
