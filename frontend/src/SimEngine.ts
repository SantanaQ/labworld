import {getWsUrl} from "./getWsUrl.ts";

export interface SimSettings {
    showHeat: boolean;
    showScent: boolean;
    showSupply: boolean;
    showAgents: boolean;
    speed: number;
}

export type ConnectionStatus = 'connected' | 'reconnecting' | 'disconnected';

export class SimEngine {
    private statusCallback: (status: ConnectionStatus) => void = () => {};

    public onStatusChange(callback: (status: ConnectionStatus) => void) {
        this.statusCallback = callback;
    }

    private gridWidth: number = 0;
    private gridHeight: number = 0;
    //private worldId: string | null = null;
    private heatData: Float32Array | null = null;
    private scentData: Float32Array | null = null;

    private canvas: HTMLCanvasElement;
    private ctx: CanvasRenderingContext2D;
    private animationFrameId: number = 0;
    private socket: WebSocket | null = null;

    private retries = 0;
    private maxRetries = 10;
    private reconnectTimeout: ReturnType<typeof setTimeout> | null = null;

    public settings: SimSettings = {
        showHeat: true,
        showScent: true,
        showSupply: true,
        showAgents: true,
        speed: 1
    };

    constructor(canvas: HTMLCanvasElement) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d')!;
    }


    public reconfigure(width: number, height: number) {
        this.gridWidth = width;
        this.gridHeight = height;

        /*this.heatData = null;
        this.foodData = null;
        this.agentData = null;*/

        this.heatData = new Float32Array(width * height);
        this.scentData = new Float32Array(width * height);

        // TEST: Ein paar Zufallswerte einfüllen, damit wir was sehen
        for (let i = 0; i < this.heatData.length; i++) {
            this.heatData[i] = Math.random();
            this.scentData[i] = Math.random() * 0.5;
        }

        console.log(`Engine reconfigured to ${width}x${height}`);
    }

    public connect(worldId: string) {
        if (this.reconnectTimeout) clearTimeout(this.reconnectTimeout);

        if (this.socket) {
            this.socket.onclose = null;
            this.socket.onmessage = null;
            this.socket.onerror = null;
            this.socket.close();
        }

        //this.worldId = worldId;

        const wsUrl = getWsUrl(`/ws/${worldId}`);

        console.log(`Connecting to World-Stream: ${wsUrl}`);

        this.socket = new WebSocket(wsUrl);
        this.socket.binaryType = 'arraybuffer';

        this.socket.onopen = () => {
            console.log(`WS Connected to World: ${worldId}`);
            this.retries = 0;
            this.statusCallback('connected');
        };

        this.socket.onmessage = (event) => {
            if (event.data instanceof ArrayBuffer) {
                this.handleBinaryFrame(event.data);
            }
        };

        this.socket.onclose = (event) => {
            // Reconnect-Logik bleibt, nutzt aber jetzt die worldId
            if (event.code !== 1000 && this.retries < this.maxRetries) {
                this.retries++;
                const delay = Math.min(this.retries * 3000, 30000);
                this.statusCallback('reconnecting');

                console.log(`WS lost. Reconnecting to ${worldId} in ${delay/1000}s...`);
                this.reconnectTimeout = setTimeout(() => this.connect(worldId), delay);
            } else {
                this.statusCallback('disconnected');
            }
        };

        this.socket.onerror = (error) => {
            console.error("WebSocket Error:", error);
        };
    }

    private handleBinaryFrame(data: any) {
        console.log(data);
        // Save data for next draw cycle
    }

    public updateSettings(newSettings: Partial<SimSettings>) {
        this.settings = { ...this.settings, ...newSettings };
    }

    private draw() {
        const { width, height } = this.canvas;
        this.ctx.clearRect(0, 0, width, height);

        if (this.settings.showHeat) {
            this.drawHeat();
        }

        if (this.settings.showScent) {
            this.drawScent();
        }

        if (this.settings.showSupply) {
            this.drawSupply();
        }

        if (this.settings.showAgents) {
            this.drawAgents();
        }

        this.animationFrameId = requestAnimationFrame(() => this.draw());
    }

    start() { this.draw(); }
    stop() {
        this.socket?.close();
        cancelAnimationFrame(this.animationFrameId);
    }

    max = 600;

    resize(w: number, h: number) {
        this.canvas.width = w < this.max ? w : this.max;
        this.canvas.height = h < this.max ? w : this.max; ;
    }

    private offscreenCanvas = document.createElement('canvas');
    private offscreenCtx = this.offscreenCanvas.getContext('2d')!;
    private drawHeat() {
        if (!this.heatData || this.gridWidth === 0) return;

        // 1. Offscreen Canvas auf Grid-Größe setzen (z.B. 200x200)
        if (this.offscreenCanvas.width !== this.gridWidth) {
            this.offscreenCanvas.width = this.gridWidth;
            this.offscreenCanvas.height = this.gridHeight;
        }

        // 2. ImageData erstellen (ein Pixel pro Grid-Zelle)
        const imgData = this.offscreenCtx.createImageData(this.gridWidth, this.gridHeight);
        const pixels = imgData.data;

        for (let i = 0; i < this.heatData.length; i++) {
            const val = this.heatData[i];
            const p = i * 4;
            pixels[p] = 255;     // R
            pixels[p+1] = 50;    // G
            pixels[p+2] = 50;    // B
            pixels[p+3] = val * 255; // Alpha (Heat-Intensität)
        }

        // 3. Daten auf das kleine Offscreen-Canvas pushen
        this.offscreenCtx.putImageData(imgData, 0, 0);

        // 4. Das kleine Bild auf das große Haupt-Canvas "strecken"
        // Das ist extrem schnell, da die GPU das Skalieren übernimmt!
        this.ctx.drawImage(
            this.offscreenCanvas,
            0, 0, this.gridWidth, this.gridHeight, // Source
            0, 0, this.canvas.width, this.canvas.height // Destination (UI Größe)
        );
    }

    private drawScent() {
        this.ctx.fillStyle = "green";
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
    }

    private drawSupply() {
        this.ctx.fillStyle = "blue";
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
    }

    private drawAgents() {
        this.ctx.fillStyle = "orange";
        this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
    }

}