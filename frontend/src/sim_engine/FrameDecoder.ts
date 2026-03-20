export interface AgentData {
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
}


export interface FrameData {
    sessionId: string
    heat: Uint8Array
    supply: Uint8Array
    scent: Uint8Array
    agents: AgentData[]
}

let latestBuffer: ArrayBuffer | null = null;
let processing = false;

let onFrame: ((frame: FrameData) => void) | null = null;

export const setFrameHandler = (cb: (frame: FrameData) => void) => {
    onFrame = cb;
};

export const handleBinaryFrame = (buffer: ArrayBuffer) => {
    latestBuffer = buffer;

    if (!processing) {
        processLoop();
    }
};

const processLoop = async () => {
    processing = true;

    while (latestBuffer) {
        const buffer = latestBuffer;
        latestBuffer = null;

        try {
            const ds = new DecompressionStream("gzip");
            const stream = new Response(buffer).body!.pipeThrough(ds);
            const decompressed = await new Response(stream).arrayBuffer();

            const frame = parseFrame(decompressed);

            if (onFrame) {
                onFrame(frame);
            }

        } catch (e) {
            console.warn("Frame failed", e);
        }
    }

    processing = false;
};



const parseFrame = (buffer: ArrayBuffer): FrameData => {
    const view = new DataView(buffer);

    let offset = 0;

    const msb = view.getBigUint64(offset, false);
    offset += 8;
    const lsb = view.getBigUint64(offset, false);
    offset += 8;

    const sessionId = formatUUID(msb, lsb);

    const width = view.getInt32(offset, true);
    offset += 4;
    const height = view.getInt32(offset, true);
    offset += 4;

    const cellCount = width * height;

    const heat = new Uint8Array(buffer, offset, cellCount);
    offset += cellCount;

    const supply = new Uint8Array(buffer, offset, cellCount);
    offset += cellCount;

    const scent = new Uint8Array(buffer, offset, cellCount);
    offset += cellCount;

    const agents = decodeAgents(buffer, offset);

    return {
        sessionId,
        heat,
        supply,
        scent,
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

const decodeAgents = (buffer: ArrayBuffer, offset: number) => {

    const stride = 22;

    if ((buffer.byteLength - offset) % stride !== 0) {
        console.warn("Agent buffer misaligned");
    }

    const agentCount = Math.floor((buffer.byteLength - offset) / stride);

    const view = new DataView(buffer);

    const agents = [] as AgentData[];

    for(let i = 0; i < agentCount; i++){

        const base = offset + i * stride;

        const posX = view.getFloat32(base, true);
        const posY = view.getFloat32(base + 4, true);

        const vX = view.getFloat32(base + 8, true);
        const vY = view.getFloat32(base + 12, true);

        const speed = view.getUint8(base + 16);
        const energy = view.getUint8(base + 17);

        const hunger = view.getUint8(base + 18);
        const heat = view.getUint8(base + 19);
        const curiosity = view.getUint8(base + 20);
        const fear = view.getUint8(base + 21);

        agents.push({posX,posY,vX,vY,speed,energy, hunger,heat,curiosity,fear});
    }

    return agents;
}