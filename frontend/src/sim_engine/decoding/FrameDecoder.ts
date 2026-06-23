import type {SimData} from "../SimData.ts";
import {ChunkID} from "./ChunkID.ts";
import {BinaryReader} from "./BinaryReader.ts";

const supportedVersion = 1;

export interface AgentData {
    id: number
    posX: number
    posY: number
    vX: number
    vY: number
    speed: number
}

interface FrameHeader {
    protocolVersion: number
    sessionId: string
    worldWidth: number
    worldHeight: number
    isFullFrame: boolean
    chunkCount: number
}

interface ChunkHeader {
    chunkId: number
    chunkSize: number
    stride: number
}

const queue: ArrayBuffer[] = [];
let processing = false;

export const handleBinaryFrame = (
    buffer: ArrayBuffer,
    simData: SimData) => {

    queue.push(buffer);

    if (!processing) {
        processLoop(simData);
    }
};

const processLoop = async (simData: SimData) => {
    processing = true;

    while (queue.length > 0) {
        const buffer = queue.shift()!;

        try {
            const decompressed = await decompress(buffer);
            const view = new DataView(decompressed);
            const binaryReader = new BinaryReader(view, true);
            parseFrame(binaryReader, simData);

        } catch (e) {
            console.warn("Frame failed", e);
        }
    }

    processing = false;
};

const decompress = async (buffer: ArrayBuffer): Promise<ArrayBuffer> => {
    const ds = new DecompressionStream("gzip");
    const stream = new Response(buffer).body!.pipeThrough(ds);
    return await new Response(stream).arrayBuffer();
};

const parseFrame = (reader: BinaryReader, simData: SimData) => {
    const frameHeader = parseHeader(reader);
    if(frameHeader.protocolVersion > supportedVersion) {
        throw new Error("Protocol Version is not supported.")
    }

    for (let i = 0; i < frameHeader.chunkCount; i++) {
        const chunkHeader = parseChunkHeader(reader);
        decodeChunk(reader, chunkHeader, simData);
    }
}

const parseHeader = (reader: BinaryReader): FrameHeader => {

    const version = reader.uint8();
    const uuidMsb = reader.uint64();
    const uuidLsb = reader.uint64();
    const sessionId = formatUUID(uuidMsb, uuidLsb);
    const worldWidth = reader.uint32();
    const worldHeight = reader.uint32();
    const isFullFrame = reader.uint8() === 1;
    const chunkCount = reader.uint8();

    return {
        protocolVersion: version,
        sessionId: sessionId,
        worldWidth: worldWidth,
        worldHeight: worldHeight,
        isFullFrame: isFullFrame,
        chunkCount: chunkCount
    }
}

const parseChunkHeader = (reader: BinaryReader): ChunkHeader => {

    const chunkId = reader.uint8();
    const chunkSize = reader.uint32();
    const stride = reader.uint8();

    return {
        chunkId: chunkId,
        chunkSize: chunkSize,
        stride: stride
    }
}

const decodeChunk = (
    reader: BinaryReader,
    chunkHeader: ChunkHeader,
    simData : SimData) => {

    switch (chunkHeader.chunkId) {
        case ChunkID.HEAT_FULL:
            copyLayer(reader, chunkHeader, simData.getLayer("heat").data);
            break;
        case ChunkID.SUPPLY_FULL:
            copyLayer(reader, chunkHeader, simData.getLayer("supply").data);
            break;
        case ChunkID.SCENT_FULL:
            copyLayer(reader, chunkHeader, simData.getLayer("scent").data);
            break;
        case ChunkID.TRAILS_FULL:
            copyLayer(reader, chunkHeader, simData.getLayer("trail").data);
            break;
        case ChunkID.STRESS_FULL:
            copyLayer(reader, chunkHeader, simData.getLayer("stress").data);
            break;

        case ChunkID.HEAT_DELTA:
            applyDelta(reader, chunkHeader, simData.getLayer("heat").data);
            break;
        case ChunkID.SUPPLY_DELTA:
            applyDelta(reader, chunkHeader, simData.getLayer("supply").data);
            break;
        case ChunkID.SCENT_DELTA:
            applyDelta(reader, chunkHeader, simData.getLayer("scent").data);
            break;
        case ChunkID.TRAILS_DELTA:
            applyDelta(reader, chunkHeader, simData.getLayer("trail").data);
            break;
        case ChunkID.STRESS_DELTA:
            applyDelta(reader, chunkHeader, simData.getLayer("stress").data);
            break;

        case ChunkID.AGENTS_CHUNK:
            decodeAgents(reader, chunkHeader, simData);
            break;
    }
}

const copyLayer = (
    reader: BinaryReader,
    chunkHeader: ChunkHeader,
    target: Uint8Array
) => {
    const cellCount = chunkHeader.chunkSize / chunkHeader.stride;
    target.set(reader.uint8Array(cellCount));
};

const applyDelta = (
    reader: BinaryReader,
    chunkHeader: ChunkHeader,
    target: Uint8Array
) => {

    const chunkSize = chunkHeader.chunkSize;
    const stride = chunkHeader.stride;

    const valueCount = chunkSize / stride;
    for (let i = 0; i < valueCount; i++) {
        const index = reader.uint32();
        const value = reader.uint8();

        if (index >= 0 && index < target.length) {
            target[index] = value;
        }
    }
};

const decodeAgents = (
    reader: BinaryReader,
    chunkHeader: ChunkHeader,
    simData: SimData) => {

    const stride = chunkHeader.stride;
    const chunkSize = chunkHeader.chunkSize;

    const agentCount = chunkSize / stride;
    const agents: AgentData[] = [];
    for (let i = 0; i < agentCount; i++) {
        agents.push({
            id: reader.uint16(),
            posX: reader.float32(),
            posY: reader.float32(),
            vX: reader.float32(),
            vY: reader.float32(),
            speed: reader.uint8(),
        });
    }

    simData.setAgents(agents);
};

const formatUUID = (
    msb: bigint,
    lsb: bigint):
    string => {

    const s = msb.toString(16).padStart(16, '0') + lsb.toString(16).padStart(16, '0');

    return [
        s.slice(0, 8),
        s.slice(8, 12),
        s.slice(12, 16),
        s.slice(16, 20),
        s.slice(20)
    ].join('-');
};


