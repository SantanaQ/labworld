package com.api.session;

import com.api.service.FrameEncoder;
import com.api.service.frame_layouts.AgentLayout;
import com.api.service.frame_layouts.WorldLayout;
import com.api.ws.WebSocketBroadcaster;
import com.sim.config.WorldConfig;
import com.sim.snapshot.WorldSnapshot;
import com.sim.world.World;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class SessionContext {

    private final UUID id;
    private final WorldConfig config;

    private World world;
    private WorldSnapshot snapshot;
    private FrameEncoder encoder;

    private final WebSocketBroadcaster broadcaster;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread thread;

    private final int tickBase = 30;
    private int ticksPerSecond = tickBase;
    private long tickNanos = 1_000_000_000L / ticksPerSecond;

    public SessionContext(WorldConfig config, WebSocketBroadcaster broadcaster) {
        this.id = UUID.randomUUID();
        this.config = config;
        this.broadcaster = broadcaster;
    }

    public UUID id() {
        return id;
    }

    public void start() {
        world = new World(config);
        snapshot = new WorldSnapshot(world);

        encoder = new FrameEncoder(
                new WorldLayout(
                        config.layerCount(),
                        config.width(),
                        config.height(),
                        config.agentCount(),
                        AgentLayout.STRIDE
                )
        );

        running.set(true);

        thread = new Thread(this::loop);
        thread.start();
    }

    private void loop() {
        long lastTime = System.nanoTime();
        long accumulator = 0;

        while (running.get()) {
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
                snapshot.refresh(world);
                broadcaster.broadcast(id, encoder.encode(snapshot));
            }
        }
    }

    public void pause() {
        running.set(false);
    }

    public void resume() {
        if(!running.get()) {
            running.set(true);
            thread = new Thread(() -> {
                try {
                    loop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }
    }

    public void stop() {
        running.set(false);
        if (thread != null) thread.interrupt();
    }

    public boolean isRunning() {
        return running.get();
    }

    public void applySpeed(double speed) {
        speed = Math.clamp(speed, 0.1, 2);
        this.ticksPerSecond = (int) (speed * tickBase);
        this.tickNanos = 1_000_000_000L / ticksPerSecond;
    }

    public void sendInitialFrame() {
        WorldSnapshot snapshot = new WorldSnapshot(world);
        ByteBuffer frame = encoder.encode(snapshot);
        broadcaster.broadcast(id, frame);
    }

}
