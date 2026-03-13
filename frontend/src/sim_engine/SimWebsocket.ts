import {getWsUrl} from "./getWsUrl.ts";
import type {ConnectionStatus} from "./SimEngine.ts";

export class SimWebSocket {
    private socket: WebSocket | null = null;
    private retries = 0;
    private maxRetries = 10;
    private reconnectTimeout: ReturnType<typeof setTimeout> | null = null;

    private worldId: string;
    private onFrame: (frame: ArrayBuffer) => void;
    private onStatus: (status: ConnectionStatus) => void;

    constructor(
        worldId: string,
        onFrame: (frame: ArrayBuffer) => void,
        onStatus: (status: ConnectionStatus) => void
    ) {
        this.worldId = worldId;
        this.onFrame = onFrame;
        this.onStatus = onStatus;
    }

    public connect() {
        if (this.socket) this.socket.close();
        const wsUrl = getWsUrl(`/ws/sim/${this.worldId}`);
        this.socket = new WebSocket(wsUrl);
        this.socket.binaryType = 'arraybuffer';

        this.socket.onopen = () => {
            this.retries = 0;
            this.onStatus('connected');
        };

        this.socket.onmessage = (e) => {
            if (e.data instanceof ArrayBuffer) this.onFrame(e.data);
        };

        this.socket.onclose = (event) => {
            if (event.code !== 1000 && this.retries < this.maxRetries) {
                this.retries++;
                const delay = Math.min(this.retries * 3000, 30000);
                this.onStatus('reconnecting');
                this.reconnectTimeout = setTimeout(() => this.connect(), delay);
            } else {
                this.onStatus('disconnected');
            }
        };

        this.socket.onerror = (err) => console.error(err);
    }

    public disconnect() {
        if (this.socket) this.socket.close();
        if (this.reconnectTimeout) clearTimeout(this.reconnectTimeout);
    }
}