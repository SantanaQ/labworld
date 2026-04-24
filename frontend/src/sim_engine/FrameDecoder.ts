import type {LayerContainer} from "./LayerContainer.ts";

export interface AgentData {
    id: number
    posX: number
    posY: number
    vX: number
    vY: number
    speed: number
    energy: number
    hunger: number
    heat: number
    curiosity: number
    fear: number
    angle: number | null
    stretch: number | null
}

export interface FrameData {
    sessionId: string
    heat: Uint8Array
    supply: Uint8Array
    scent: Uint8Array
    agents: AgentData[]
}


const queue: ArrayBuffer[] = [];
let processing = false;

export const handleBinaryFrame = (buffer: ArrayBuffer, layers: LayerContainer) => {
    queue.push(buffer);

    if (!processing) {
        processLoop(layers);
    }
};

const processLoop = async (layers: LayerContainer) => {
    processing = true;

    while (queue.length > 0) {
        const buffer = queue.shift()!;

        try {
            const decompressed = await decompress(buffer);
            parseFrame(decompressed, layers);

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

const parseFrame = (buffer: ArrayBuffer, layers: LayerContainer): FrameData => {
    const view = new DataView(buffer);
    let offset = 0;

    const require = (bytes: number) => {
        if (offset + bytes > buffer.byteLength) {
            throw new Error("Buffer underflow");
        }
    };

    require(16);
    const msb = view.getBigUint64(offset, true); offset += 8;
    const lsb = view.getBigUint64(offset, true); offset += 8;

    const sessionId = formatUUID(msb, lsb);

    require(8);
    const width = view.getInt32(offset, true); offset += 4;
    const height = view.getInt32(offset, true); offset += 4;

    require(1);
    const isFullFrame = view.getUint8(offset) === 1;
    offset += 1;

    const cellCount = width * height;

    if (isFullFrame) {
        require(cellCount * 3);

        copyLayer(buffer, offset, cellCount, layers.getLayer("heat").data);
        offset += cellCount;

        copyLayer(buffer, offset, cellCount, layers.getLayer("supply").data);
        offset += cellCount;

        copyLayer(buffer, offset, cellCount, layers.getLayer("scent").data);
        offset += cellCount;

    } else {
        offset = applyDelta(view, offset, layers.getLayer("heat").data);
        offset = applyDelta(view, offset, layers.getLayer("supply").data);
        offset = applyDelta(view, offset, layers.getLayer("scent").data);
    }

    const agents = decodeAgents(buffer, offset);
    layers.setAgents(agents);

    return {
        sessionId,
        heat: layers.getLayer("heat").data,
        supply: layers.getLayer("supply").data,
        scent: layers.getLayer("scent").data,
        agents,
    };
};

const formatUUID = (msb: bigint, lsb: bigint): string => {
    const s = msb.toString(16).padStart(16, '0') + lsb.toString(16).padStart(16, '0');

    return [
        s.slice(0, 8),
        s.slice(8, 12),
        s.slice(12, 16),
        s.slice(16, 20),
        s.slice(20)
    ].join('-');
};

const copyLayer = (
    buffer: ArrayBuffer,
    offset: number,
    size: number,
    target: Uint8Array
) => {
    target.set(new Uint8Array(buffer, offset, size));
};

const applyDelta = (
    view: DataView,
    offset: number,
    target: Uint8Array
): number => {

    if (offset + 4 > view.byteLength) return offset;

    const size = view.getInt32(offset, true);
    offset += 4;

    if (size < 0 || size > target.length) {
        console.warn("Invalid delta size", size);
        return offset;
    }

    for (let i = 0; i < size; i++) {
        if (offset + 5 > view.byteLength) break;

        const index = view.getInt32(offset, true);
        offset += 4;

        const value = view.getUint8(offset);
        offset += 1;

        if (index >= 0 && index < target.length) {
            target[index] = value;
        }
    }

    return offset;
};

const decodeAgents = (buffer: ArrayBuffer, offset: number): AgentData[] => {
    const view = new DataView(buffer);

    // First byte is byte size per agent
    const stride = view.getUint8(offset);
    offset++;
    const remaining = buffer.byteLength - offset;

    if (remaining < 0) return [];

    const count = Math.floor(remaining / stride);


    const agents: AgentData[] = [];

    for (let i = 0; i < count; i++) {
        const base = offset + i * stride;


        agents.push({
            id: view.getUint16(base, true),
            posX: view.getFloat32(base + 2, true),
            posY: view.getFloat32(base + 6, true),
            vX: view.getFloat32(base + 10, true),
            vY: view.getFloat32(base + 14, true),
            speed: view.getUint8(base + 18),
            energy: view.getUint8(base + 19),
            hunger: view.getUint8(base + 20),
            heat: view.getUint8(base + 21),
            curiosity: view.getUint8(base + 22),
            fear: view.getUint8(base + 23),
            angle: null,
            stretch: null
        });
    }

    return agents;
};

