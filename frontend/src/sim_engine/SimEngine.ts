import {LayerContainer, type LayerName} from "./LayerContainer.ts";
import { CanvasRenderer } from "./CanvasRenderer.ts";
import {SimWebSocket} from "./SimWebsocket.ts";
import type {WorldConfig} from "../pages/Dashboard.tsx";
import type {Camera} from "../hooks/useCanvasCamera.ts";
import React from "react";
import {handleBinaryFrame} from "./FrameDecoder.ts";

export type ConnectionStatus = 'connected' | 'reconnecting' | 'disconnected';

export interface SimSettings {
    showHeat: boolean;
    showScent: boolean;
    showSupply: boolean;
    showAgents: boolean;
    speed: number;
}


export class SimEngine {
    private layers = new LayerContainer();
    private renderer: CanvasRenderer;
    private ws: SimWebSocket | null = null;
    private animationFrameId = 0;


    public settings: SimSettings = {
        showHeat: true,
        showScent: true,
        showSupply: true,
        showAgents: true,
        speed: 1,
    };

    constructor(canvas: HTMLCanvasElement,
                cameraRef : React.RefObject<Camera>) {
        this.renderer = new CanvasRenderer(canvas, cameraRef);
    }

    public reconfigure(config: WorldConfig) {
        ['heat', 'scent', 'supply'].forEach((name) => {
            this.layers.setLayer(name as LayerName, config.width, config.height);
        });
    }

    public connect(sessionId: string) {
        if (this.ws?.isOpen()) return;
        this.ws = new SimWebSocket(sessionId, (frame) => handleBinaryFrame(frame, this.layers), (status) => this.logStatus(status));

        this.ws.connect();
    }


    private async logStatus(status : ConnectionStatus) {
        console.log('WS status:', status);
    }


    public updateSettings(settings: Partial<SimSettings>) {
        this.settings = { ...this.settings, ...settings };
    }

    private draw = () => {
        this.renderer.clear();

        if (this.settings.showHeat)
            this.renderer.drawLayer(this.layers.getLayer("heat"), this.renderer.heatLUT, "lighter");

        if (this.settings.showSupply)
            this.renderer.drawLayer(this.layers.getLayer("supply"), this.renderer.supplyLUT, "screen");

        if (this.settings.showScent)
            this.renderer.drawLayer(this.layers.getLayer("scent"), this.renderer.scentLUT, "lighter");

        if (this.settings.showAgents)
            this.renderer.drawAgents(this.layers.getAgents()) ;

        this.animationFrameId = requestAnimationFrame(this.draw);
    }

    public start() {
        this.draw();
    }

    public stop() {
        this.renderer.clear();
        cancelAnimationFrame(this.animationFrameId);
        this.ws?.disconnect();
    }

    public findAgentByCoordinate(posX: number, posY: number) {
        const tolerance = 0.5;
        const agents = this.layers.getAgents();

        for (const agent of agents) {
            if (
                Math.abs(posX - agent.posX) <= tolerance &&
                Math.abs(posY - agent.posY) <= tolerance
            ) {
                return agent;
            }
        }

        return null;
    }


}