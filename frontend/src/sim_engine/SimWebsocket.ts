import {getWsUrl} from "./getWsUrl.ts";
import type {ConnectionStatus} from "./SimEngine.ts";

export class SimWebSocket {
    private socket: WebSocket | null = null;
    private retries = 0;
    private maxRetries = 10;
    private reconnectTimeout: ReturnType<typeof setTimeout> | null = null;

    private sessionId: string;
    private onFrame: (frame: ArrayBuffer) => void;
    private onStatus: (status: ConnectionStatus) => void;

    private intentionalClose = false;

    constructor(
        sessionId: string,
        onFrame: (frame: ArrayBuffer) => void,
        onStatus: (status: ConnectionStatus) => void
    ) {
        this.sessionId = sessionId;
        this.onFrame = onFrame;
        this.onStatus = onStatus;
    }

    public connect() {
        if (this.socket?.readyState === WebSocket.OPEN) return;
        this.intentionalClose = true;
        const wsUrl = getWsUrl(`/ws/sim/${this.sessionId}`);
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
            if (this.intentionalClose) {
                this.onStatus('disconnected');
                return;
            }

            const shouldReconnect =
                event.code !== 1000 &&
                event.code !== 1001 &&
                this.retries < this.maxRetries;

            if (shouldReconnect) {
                this.retries++;
                this.onStatus('reconnecting');

                this.reconnectTimeout = setTimeout(() => this.connect(), 3000);
            } else {
                this.onStatus('disconnected');
            }
        };

        this.socket.onerror = (err) => console.error(err);
    }

    public disconnect() {
        this.intentionalClose = true;
        if (this.socket) this.socket.close();
        if (this.reconnectTimeout) clearTimeout(this.reconnectTimeout);
    }

    public isOpen() {
        return this.socket?.readyState === WebSocket.OPEN;
    }

}