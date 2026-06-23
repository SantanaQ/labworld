package com.api.encoding;

import com.sim.config.WorldConfig;
import com.sim.layer.LayerID;
import com.sim.snapshot.WorldSnapshot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Binary frame encoder for simulation snapshots.
 *
 * <h2>Protocol Specification — Version {@value #PROTOCOL_VERSION}</h2>
 *
 * <p>All values are encoded in <b>little-endian</b> byte order.</p>
 *
 * <h3>Frame Layout</h3>
 *
 * <pre>
 * +-------------------+
 * | Frame Header      |
 * +-------------------+
 * | Chunk 1           |
 * +-------------------+
 * | Chunk 2           |
 * +-------------------+
 * | ...               |
 * +-------------------+
 * | Chunk N           |
 * +-------------------+
 * </pre>
 *
 * <p>
 * A frame consists of one frame header followed by a variable number of chunks.
 * Chunks contain either layer data or agent data.
 * </p>
 *
 * <h3>Frame Header (27 bytes)</h3>
 *
 * <pre>
 * Offset | Size | Type   | Description
 * -------+------+--------+-----------------------------
 * 0      | 1    | uint8  | Protocol version
 * 1      | 16   | UUID   | World ID
 * 17     | 4    | int32  | World width
 * 21     | 4    | int32  | World height
 * 25     | 1    | uint8  | Encoding type
 * 26     | 1    | uint8  | Chunk count
 * </pre>
 *
 * <h4>Protocol Version</h4>
 * <ul>
 *   <li>1 = Protocol version 1.0</li>
 * </ul>
 *
 * <h4>Encoding Type</h4>
 * <ul>
 *   <li>0 = Delta frame</li>
 *   <li>1 = Full frame</li>
 * </ul>
 *
 * <h3>Chunk Header (6 bytes)</h3>
 *
 * <pre>
 * Offset | Size | Type   | Description
 * -------+------+--------+-----------------------------
 * 0      | 1    | uint8  | Chunk ID
 * 1      | 4    | int32  | Chunk payload size in bytes
 * 5      | 1    | uint8  | Entry stride in bytes
 * </pre>
 *
 * <h3>Chunk Types</h3>
 *
 * <p>Chunk IDs are defined in {@link ChunkID}.</p>
 *
 * <h4>Full Layer Chunk</h4>
 *
 * <p>
 * Contains a complete simulation layer.
 * Each cell is quantized to one byte.
 * </p>
 *
 * <pre>
 * Entry:
 * +--------+
 * | value  |
 * +--------+
 * | uint8  |
 * </pre>
 *
 * <ul>
 *   <li>Stride = 1 byte</li>
 *   <li>Payload size = width × height</li>
 * </ul>
 *
 * <h4>Delta Layer Chunk</h4>
 *
 * <p>
 * Contains only changed cells of a simulation layer.
 * </p>
 *
 * <pre>
 * Entry:
 * +------------+--------+
 * | index      | value  |
 * +------------+--------+
 * | int32      | uint8  |
 * </pre>
 *
 * <ul>
 *   <li>Stride = 5 bytes</li>
 * </ul>
 *
 * <h4>Agent Chunk</h4>
 *
 * <p>
 * Contains a full snapshot of all agents.
 * </p>
 *
 * <pre>
 * Entry:
 * +------+--------+--------+--------+--------+--------+
 * | id   | posX   | posY   | velX   | velY   | speed  |
 * +------+--------+--------+--------+--------+--------+
 * | u16  | float  | float  | float  | float  | uint8  |
 * </pre>
 *
 * <ul>
 *   <li>Stride = 19 bytes</li>
 * </ul>
 *
 * <h3>Notes</h3>
 *
 * <ul>
 *   <li>All layer values are quantized from float to uint8.</li>
 *   <li>Agents are always transmitted as full snapshots.</li>
 *   <li>Delta chunk indexes use flattened row-major indexing.</li>
 * </ul>
 */
public class FrameEncoder {

    private static final byte PROTOCOL_VERSION = 1;

    private final FrameHeader frameHeader;
    private final FullLayerChunk fullLayerChunk;
    private final DeltaLayerChunk deltaLayerChunk;
    private final AgentChunk agentChunk;

    private final ByteBuffer buffer;

    public FrameEncoder(WorldConfig config) {
        this.frameHeader = new FrameHeader();
        this.fullLayerChunk = new FullLayerChunk();
        this.deltaLayerChunk = new DeltaLayerChunk();
        this.agentChunk = new AgentChunk();

        int headerBytes = frameHeader.totalBytes();
        int agentBytes = agentChunk.totalBytes(config.agentCount());

        int worldWidth = config.width();
        int worldHeight = config.height();
        int layerCount = config.layerCount();
        int worstCaseBytesFull = fullLayerChunk.maxBytes(worldWidth, worldHeight);
        int worstCaseBytesDelta = deltaLayerChunk.maxBytes(worldWidth, worldHeight);
        int layerBytes = layerCount * Math.max(worstCaseBytesFull, worstCaseBytesDelta);

        int totalBytes = headerBytes + agentBytes + layerBytes;
        this.buffer = ByteBuffer
                .allocateDirect(totalBytes)
                .order(ByteOrder.LITTLE_ENDIAN);
    }

    public ByteBuffer encodeFull(WorldSnapshot snap) {
        buffer.clear();

        // Header
        frameHeader.encode(buffer, snap, PROTOCOL_VERSION, true);

        // Layers
        fullLayerChunk.encode(LayerID.HEAT, ChunkID.HEAT_CHUNK, buffer, snap);
        fullLayerChunk.encode(LayerID.SUPPLY, ChunkID.SUPPLY_CHUNK, buffer, snap);
        fullLayerChunk.encode(LayerID.SCENT, ChunkID.SCENT_CHUNK, buffer, snap);
        fullLayerChunk.encode(LayerID.TRAIL, ChunkID.TRAILS_CHUNK, buffer, snap);
        fullLayerChunk.encode(LayerID.STRESS, ChunkID.STRESS_CHUNK, buffer, snap);

        // Agents
        agentChunk.encode(ChunkID.AGENTS_CHUNK, buffer, snap);

        buffer.flip();
        return buffer;
    }

    public ByteBuffer encodeDelta(WorldSnapshot snap) {
        buffer.clear();

        // Header
        frameHeader.encode(buffer, snap, PROTOCOL_VERSION, false);

        // Layers
        deltaLayerChunk.encode(LayerID.HEAT, ChunkID.HEAT_DELTA_CHUNK, buffer, snap);
        deltaLayerChunk.encode(LayerID.SUPPLY, ChunkID.SUPPLY_DELTA_CHUNK, buffer, snap);
        deltaLayerChunk.encode(LayerID.SCENT, ChunkID.SCENT_DELTA_CHUNK, buffer, snap);
        deltaLayerChunk.encode(LayerID.TRAIL, ChunkID.TRAILS_DELTA_CHUNK, buffer, snap);
        deltaLayerChunk.encode(LayerID.STRESS, ChunkID.STRESS_DELTA_CHUNK, buffer, snap);

        // Agents
        agentChunk.encode(ChunkID.AGENTS_CHUNK, buffer, snap);

        buffer.flip();
        return buffer;
    }

}
