package com.api.service;

import com.api.resource.JsonWorldConfig;
import com.api.service.frame_layouts.AgentLayout;
import com.api.service.frame_layouts.WorldLayout;
import com.api.ws.WebSocketBroadcaster;
import com.sim.config.WorldConfig;
import com.sim.snapshot.WorldSnapshot;
import com.sim.world.World;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class SimulationService {


    private WorldConfig config;
    private World world;
    private WorldSnapshot worldSnapshot;

    private final WebSocketBroadcaster broadcaster;
    private FrameEncoder encoder;

    private boolean running;
    private Thread simulationThread;

    private int tickBase = 30;
    private int ticksPerSecond = tickBase;
    private long tickNanos = 1_000_000_000L / ticksPerSecond;


    public SimulationService(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    public void setConfig(JsonWorldConfig cfg) {
        this.config = WorldConfigHandler.translateConfig(cfg);
    }

    public void setConfig(WorldConfig cfg) {
        this.config = cfg;
    }

    public void setSpeed(double speed) {
        this.ticksPerSecond = (int) (speed * tickBase);
        this.tickNanos = 1_000_000_000L / ticksPerSecond;
    }

    public void start() {
        world = new World(config);
        worldSnapshot = new WorldSnapshot(world);
        WorldLayout layout = new WorldLayout(
                config.layerCount(),
                config.width(),
                config.height(),
                config.agentCount(),
                AgentLayout.STRIDE
        );


        encoder = new FrameEncoder(layout);

        running = true;
        simulationThread = new Thread(() -> {
            try {
                loop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        simulationThread.start();
    }

    public synchronized void pause() {
        running = false;
    }

    public synchronized void resume() {
        if(!running) {
            running = true;
            simulationThread = new Thread(() -> {
                try {
                    loop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            simulationThread.start();
        }
    }

    public synchronized void stop() {
        running = false;
        interruptThread();
        clearSession();
        cleanWorldConfig();
        resetTick();
    }

    private void loop() {
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
                worldSnapshot.refresh(world);
                ByteBuffer frame = encoder.encode(worldSnapshot);
                broadcaster.broadcast(frame);
            }
        }
    }

    public WorldSnapshot previewSnapshot() {
        if(!hasConfig()) throw new IllegalStateException("No config set");
        World tempWorld = new World(config);
        return new WorldSnapshot(tempWorld);
    }

    public boolean hasConfig() {
        return config != null;
    }

    public WorldConfig config() {
        return config;
    }

    private void interruptThread() {
        if (simulationThread != null) {
            simulationThread.interrupt();
            try {
                simulationThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            simulationThread = null;
        }
    }

    private void resetTick() {
        ticksPerSecond = tickBase;
        tickNanos = 1_000_000_000L / ticksPerSecond;
    }

    private void clearSession() {
        broadcaster.clear();
    }

    private void cleanWorldConfig() {
        encoder = null;
        world = null;
        config = null;
    }

}