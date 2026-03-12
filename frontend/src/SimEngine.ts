import {getWsUrl} from "./getWsUrl.ts";

export interface SimSettings {
    showHeat: boolean;
    showScent: boolean;
    showSupply: boolean;
}

export class SimEngine {
    private canvas: HTMLCanvasElement;
    private ctx: CanvasRenderingContext2D;
    private animationFrameId: number = 0;
    private socket: WebSocket | null = null;

    public settings: SimSettings = {
        showHeat: true,
        showScent: true,
        showSupply: true
    };

    constructor(canvas: HTMLCanvasElement) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d')!;
        this.initWebsocket();
    }

    private initWebsocket() {
        this.socket = new WebSocket(getWsUrl('/ws'));

        this.socket.onclose = () => {
            console.log("WS closed. Reconnect in 3s...");
            setTimeout(() => this.initWebsocket(), 3000);
        };

        this.socket.binaryType = 'arraybuffer';

        this.socket.onmessage = (event: MessageEvent) => {
            if (event.data instanceof ArrayBuffer) {
                this.handleBinaryFrame(event.data);
            }
        };
    }

    private handleBinaryFrame(data: any) {
        console.log(data);
        // Save data for next draw cicle
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

        this.animationFrameId = requestAnimationFrame(() => this.draw());
    }

    start() { this.draw(); }
    stop() {
        this.socket?.close();
        cancelAnimationFrame(this.animationFrameId);
    }
    resize(w: number, h: number) { this.canvas.width = w; this.canvas.height = h; }
    private drawHeat() { /* ... */ }
    private drawScent() { /* ... */ }
    private drawSupply() { /* ... */ }
}