import {LayerContainer, type LayerName} from "./LayerContainer.ts";
import { CanvasRenderer } from "./CanvasRenderer.ts";
import {SimWebSocket} from "./SimWebsocket.ts";

export type ConnectionStatus = 'connected' | 'reconnecting' | 'disconnected';

export interface SimSettings {
    showHeat: boolean;
    showScent: boolean;
    showSupply: boolean;
    showAgents: boolean;
    speed: number;
}

interface AgentProps {
    x: number;
    y: number;
    vx: number;
    vy: number;
    hunger: number;
    heat: number;
    curiosity: number;
    fear: number;
    speed: number;
}

export class SimEngine {
    private layers = new LayerContainer();
    private renderer: CanvasRenderer;
    private ws: SimWebSocket | null = null;
    private animationFrameId = 0;

    private agentBuffer = new Array<AgentProps>();

    public settings: SimSettings = {
        showHeat: true,
        showScent: true,
        showSupply: true,
        showAgents: true,
        speed: 1,
    };

    constructor(canvas: HTMLCanvasElement) {
        this.renderer = new CanvasRenderer(canvas);
    }

    public reconfigure(width: number, height: number) {
        ['heat', 'scent', 'supply', 'agents'].forEach((name) => {
            this.layers.setLayer(name as LayerName, width, height);
        });
        console.log(`Engine reconfigured to ${width}x${height}`);
    }

    public connect(worldId: string) {
        this.ws = new SimWebSocket(worldId, (frame) => this.handleBinaryFrame(frame), (status) => console.log('WS status:', status));
        this.ws.connect();
    }

    private handleBinaryFrame(buffer: ArrayBuffer) {

        const view = new DataView(buffer);

        let offset = 0;

        view.getInt32(offset, true); // worldId
        offset += 4;

        const width = view.getInt32(offset, true);
        offset += 4;
        const height = view.getInt32(offset, true);
        offset += 4;

        console.log(width, height);
        const cellCount = width * height;

        const heat = new Float32Array(buffer, offset, cellCount);
        offset += cellCount * 4;

        const supply = new Float32Array(buffer, offset, cellCount);
        offset += cellCount * 4;

        const scent = new Float32Array(buffer, offset, cellCount);
        offset += cellCount * 4;

        this.layers.getLayer("heat").data.set(heat);
        this.layers.getLayer("supply").data.set(supply);
        this.layers.getLayer("scent").data.set(scent);

        this.decodeAgents(buffer, offset);
    }

    private decodeAgents(buffer: ArrayBuffer, offset: number) {

        const stride = 40;

        const agentCount = (buffer.byteLength - offset) / stride;

        const view = new DataView(buffer);

        const agents = [];

        for(let i=0;i<agentCount;i++){

            const base = offset + i*stride;

            const x = view.getFloat32(base + 0, true);
            const y = view.getFloat32(base + 4, true);

            const vx = view.getFloat32(base + 8, true);
            const vy = view.getFloat32(base + 12, true);

            const hunger = view.getFloat32(base + 16, true);
            const heat = view.getFloat32(base + 20, true);
            const curiosity = view.getFloat32(base + 24, true);
            const fear = view.getFloat32(base + 28, true);

            const speed = view.getFloat64(base + 32, true);

            agents.push({x,y,vx,vy,hunger,heat,curiosity,fear,speed});
        }

        this.agentBuffer = agents;
    }

    public updateSettings(settings: Partial<SimSettings>) {
        this.settings = { ...this.settings, ...settings };
    }

    private draw = () => {
        this.renderer.clear();

        if (this.settings.showHeat) this.renderer.drawLayer(this.layers.getLayer('heat'), (v) => [255, 50, 50, v*255]);
        if (this.settings.showSupply) this.renderer.drawLayer(this.layers.getLayer('supply'), (v) => [0, 0, 255, v*255]);
        if (this.settings.showScent) this.renderer.drawLayer(this.layers.getLayer('scent'), (v) => [0, 255, 0, v*255]);
        if (this.settings.showAgents) this.renderer.drawLayer(this.layers.getLayer('agents'), (v) => [255, 165, 0, v*255]);

        this.animationFrameId = requestAnimationFrame(this.draw);
    }

    public start() { this.draw(); }
    public stop() {
        cancelAnimationFrame(this.animationFrameId);
        this.ws?.disconnect();
    }

    public pause() {
        cancelAnimationFrame(this.animationFrameId);
    }

    public resize(w: number, h: number) { this.renderer.resize(w, h); }
}