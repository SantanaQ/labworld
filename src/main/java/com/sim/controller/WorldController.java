package com.sim.controller;

import com.sim.gui_adapters.ThreadScheduler;
import com.sim.gui_adapters.WorldView;
import com.sim.snapshot.WorldSnapshot;
import com.sim.world.World;
import javafx.animation.AnimationTimer;

public class WorldController {

    private final World world;
    private final WorldView view;
    private final ThreadScheduler threadScheduler;

    private volatile boolean running = false;

    private volatile WorldSnapshot latestSnapshot;

    private AnimationTimer renderTimer;

    final int TICKS_PER_SECOND = 30;
    final long TICK_NANOS = 1_000_000_000L / TICKS_PER_SECOND;

    public WorldController(World world, WorldView view, ThreadScheduler threadScheduler) {
        this.world = world;
        this.view = view;
        this.threadScheduler = threadScheduler;
    }

    public void start() {
        running = true;

        // Simulations-Thread
        Thread simThread = new Thread(this::runSimulation, "SimulationThread");
        simThread.setDaemon(true);
        simThread.start();

        // Rendering auf dem JavaFX-Thread starten
        threadScheduler.schedule(this::startRendering);
    }


    private void runSimulation() {
        long lastTime = System.nanoTime();
        long accumulator = 0;

        while (running) {
            long now = System.nanoTime();
            long delta = now - lastTime;
            lastTime = now;

            accumulator += delta;

            boolean ticked = false;

            while (accumulator >= TICK_NANOS) {
                world.tick();
                accumulator -= TICK_NANOS;
                ticked = true;
            }

            // Snapshot nur erzeugen, wenn sich die Welt geändert hat
            if (ticked) {
                latestSnapshot = new WorldSnapshot(world);
                view.updateSnapshot(latestSnapshot);
            }

            // CPU schonen (kein Busy-Wait)
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    // -----------------------------
    // Rendering (JavaFX-Thread)
    // -----------------------------
    private void startRendering() {
        renderTimer = new AnimationTimer() {

            private long last = 0;

            @Override
            public void handle(long now) {
                // ~60 FPS Limit
                if (now - last < 16_000_000) return;
                last = now;

                WorldSnapshot snap = latestSnapshot;
                if (snap != null) {
                    view.render(snap);
                }
            }
        };

        renderTimer.start();
    }

    // Optional: sauber stoppen
    public void stop() {
        running = false;

        if (renderTimer != null) {
            renderTimer.stop();
        }
    }
}
