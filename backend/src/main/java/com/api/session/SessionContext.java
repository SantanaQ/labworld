package com.api.session;

import com.api.service.FrameEncoder;
import com.api.service.Gzip;
import com.api.service.frame_layouts.AgentLayout;
import com.api.service.frame_layouts.WorldLayout;
import com.api.ws.WebSocketBroadcaster;
import com.sim.config.WorldConfig;
import com.sim.snapshot.WorldSnapshot;
import com.sim.world.World;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.Deflater;

public class SessionContext {

    private final UUID id;

    private final World world;

    private final Object bufferLock = new Object();
    private volatile  WorldSnapshot frontBuffer;
    private  WorldSnapshot backBuffer;

    private final FrameEncoder encoder;

    private final WebSocketBroadcaster broadcaster;

    private final AtomicBoolean running = new AtomicBoolean(false);

    private ExecutorService simExecutor;
    private ExecutorService broadcastExecutor;

    private final int tickBase = 30;
    private long tickNanos = 1_000_000_000L / tickBase;
    private final int broadcastFps = 20;
    private final long broadcastNanos = 1_000_000_000L / broadcastFps;

    public SessionContext(WorldConfig config, WebSocketBroadcaster broadcaster) {
        this.id = UUID.randomUUID();
        this.broadcaster = broadcaster;

        world = new World(config);
        frontBuffer = new WorldSnapshot(world);
        backBuffer = new WorldSnapshot(world);


        /*encoder = new FrameEncoder(
                new WorldLayout(
                        config.layerCount(),
                        config.width(),
                        config.height(),
                        config.agentCount(),
                        AgentLayout.STRIDE
                )
        );*/
        encoder = new FrameEncoder(config);

    }

    public UUID id() {
        return id;
    }

    public void start() {
        if (running.get()) return;
        running.set(true);

        simExecutor = Executors.newSingleThreadExecutor();
        broadcastExecutor = Executors.newSingleThreadExecutor();

        simExecutor.submit(this::runSimulation);
        broadcastExecutor.submit(this::runBroadcast);
    }

    private void runSimulation() {
        while (running.get()) {
            long startTime = System.nanoTime();

            world.tick();
            backBuffer.refresh(world);

            synchronized (bufferLock) {
                swapBuffers();
            }

            long elapsed = System.nanoTime() - startTime;
            long sleepNanos = tickNanos - elapsed;

            if (sleepNanos > 0) {
                sleepPrecise(sleepNanos);
            }
        }
    }

    private void runBroadcast() {
        while (running.get()) {
            try {
                long startTime = System.nanoTime();

                byte[] payload = copyAndCompressBuffer();

                broadcaster.send(id.toString(), payload);

                long elapsed = System.nanoTime() - startTime;
                long sleepNanos = broadcastNanos - elapsed;

                if (sleepNanos > 0) {
                    sleepPrecise(sleepNanos);
                }
            } catch (Exception e) {
                System.err.println("Broadcast loop crashed: " + e.getMessage());
            }

        }
    }

    private void swapBuffers() {
        WorldSnapshot tmp = frontBuffer;
        frontBuffer = backBuffer;
        backBuffer = tmp;
    }

    public void pause() {
        running.set(false);
    }

    public void resume() {
        if(!isRunning()) {
            start();
        }
    }

    public void stop() {
        running.set(false);
        if (simExecutor != null) simExecutor.shutdownNow();
        if (broadcastExecutor != null) broadcastExecutor.shutdownNow();
    }

    public boolean isRunning() {
        return running.get();
    }

    private void sleepPrecise(long nanos) {
        try {
            long millis = nanos / 1_000_000;
            int remainingNanos = (int) (nanos % 1_000_000);
            Thread.sleep(millis, remainingNanos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void applySpeed(double speed) {
        speed = Math.clamp(speed, 0.1, 2.0);
        this.tickNanos = (long) (1_000_000_000L / (tickBase * speed));
    }

    public void sendInitialFrame() {
        backBuffer.refresh(world);
        swapBuffers();
        try {
            byte[] payload = copyAndCompressBuffer();
            broadcaster.send(id.toString(), payload);
        } catch (IOException e) {
            System.err.println("Failed to send initial frame: " + e.getMessage());
        }

    }

    private byte[] copyAndCompressBuffer() throws IOException {
        byte[] data;

        synchronized (bufferLock) {
            ByteBuffer buffer = encoder.encode(frontBuffer);

            ByteBuffer copy = buffer.asReadOnlyBuffer();
            data = new byte[copy.remaining()];
            copy.get(data);
        }

        return Gzip.compress(data);
    }

}
