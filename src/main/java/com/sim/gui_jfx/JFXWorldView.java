package com.sim.gui_jfx;

import com.sim.gui_adapters.WorldView;
import com.sim.snapshot.WorldSnapshot;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class JFXWorldView implements WorldView {

    private final StackPane root;


    private final List<RenderLayer> layers = new ArrayList<>();

    private final WorldAgents agents;

    private int worldWidth = 200;
    private int worldHeight = 200;

    private volatile WorldSnapshot latestSnapshot;

    public JFXWorldView() {

        root = new StackPane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));


        WorldHeat heat = new WorldHeat();
        WorldFood food = new WorldFood();
        WorldScent scent = new WorldScent();
        agents = new WorldAgents();

        //layers.add(heat);
        layers.add(food);
        layers.add(scent);
        layers.add(agents);

        root.getChildren().addAll(heat.canvas(), food.canvas(), agents.canvas());

        DoubleBinding sizeBinding = Bindings.createDoubleBinding(
                () -> Math.min(root.getWidth(), root.getHeight()),
                root.widthProperty(), root.heightProperty()
        );
        for (RenderLayer layer : layers) {
            layer.canvas().widthProperty().bind(sizeBinding);
            layer.canvas().heightProperty().bind(sizeBinding);
        }

        root.layoutBoundsProperty().addListener((obs, old, bounds) -> updateCellSize());

        Platform.runLater(this::updateCellSize);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                WorldSnapshot snap = latestSnapshot;
                if (snap != null) {
                    render(snap);
                }
            }
        };
        timer.start();
    }

    public Parent root() {
        return root;
    }

    @Override
    public void updateSnapshot(WorldSnapshot snapshot) {
        this.latestSnapshot = snapshot;

        this.worldWidth = snapshot.width();
        this.worldHeight = snapshot.height();

        updateCellSize();
    }

    @Override
    public void render(WorldSnapshot worldSnap) {

        for (RenderLayer layer : layers) {
            layer.clear();
        }

        for (int y = 0; y < worldHeight; y++) {
            for (int x = 0; x < worldWidth; x++) {
                for (RenderLayer layer : layers) {
                    layer.renderCell(worldSnap, x, y);
                }
            }
        }

        agents.renderAll(worldSnap);

    }

    private void updateCellSize() {
        /*double canvasW = layers.getFirst().canvas().getWidth();
        double canvasH = layers.getFirst().canvas().getHeight();

        if (canvasW <= 0 || canvasH <= 0) return;

        double size = Math.min(canvasW / worldWidth, canvasH / worldHeight);
        App.CELL_SIZE = Math.max(1, (int) Math.floor(size));*/
    }
}
