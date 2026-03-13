package com.api.service;

import com.api.resource.JsonWorldConfig;
import com.api.ws.WebSocketBroadcaster;
import com.sim.config.WorldConfig;
import com.sim.snapshot.WorldSnapshot;
import com.sim.world.World;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

    private WorldConfig config;
    private World world;

    private final WebSocketBroadcaster broadcaster;
    private FrameEncoder encoder;

    private boolean running;

    private int ticksPerSecond = 30;
    private long tickNanos = 1_000_000_000L / ticksPerSecond;


    public SimulationService(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    public void setConfig(JsonWorldConfig config) {
        this.config = WorldConfigHandler.translateConfig(config);
    }

    public void setSpeed(double speed) {
        this.ticksPerSecond = (int) (speed * ticksPerSecond);
        this.tickNanos = 1_000_000_000L / ticksPerSecond;
    }

    public void start() {
        world = new World(config);
        FrameLayout layout = new FrameLayout(
                config.layerCount(),
                config.width(),
                config.height(),
                config.agentCount(),
                config.agentStride()
        );

        encoder = new FrameEncoder(layout);

        running = true;
        new Thread(() -> {
            try {
                loop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void pause() {
    }

    private void loop() throws Exception {
        long lastTime = System.nanoTime();
        long accumulator = 0;

        while(running) {
            long now = System.nanoTime();
            long delta = now - lastTime;
            lastTime = now;

            accumulator += delta;

            boolean ticked = false;

            while (accumulator >= tickNanos) {
                world.tick();
                accumulator -= tickNanos;
                ticked = true;
            }

            if (ticked) {
                WorldSnapshot snap  = new WorldSnapshot(world);
                encoder.encode(snap);
                broadcaster.broadcast(encoder.currentFrame());
            }


        }
    }

}