import {SimData, type LayerName} from "./SimData.ts";
import { CanvasRenderer } from "./CanvasRenderer.ts";
import {SimWebSocket} from "./SimWebsocket.ts";
import type {WorldConfig} from "../pages/Dashboard.tsx";
import type {Camera} from "../hooks/useCanvasCamera.ts";
import React from "react";
import {handleBinaryFrame} from "./decoding/FrameDecoder.ts";

export type ConnectionStatus = 'connected' | 'reconnecting' | 'disconnected';

export interface SimSettings {
    showHeat: boolean;
    showScent: boolean;
    showTrails: boolean;
    showStress: boolean;
    showSupply: boolean;
    showAgents: boolean;
    speed: number;
}


export class SimEngine {
    private layers = new SimData();
    private renderer: CanvasRenderer;
    private ws: SimWebSocket | null = null;
    private animationFrameId = 0;
    private running = false;

    public settings: SimSettings = {
        showHeat: true,
        showScent: true,
        showTrails: true,
        showStress: true,
        showSupply: true,
        showAgents: true,
        speed: 1,
    };

    constructor(canvas: HTMLCanvasElement,
                cameraRef : React.RefObject<Camera>) {
        this.renderer = new CanvasRenderer(canvas, cameraRef);
    }

    public reconfigure(config: WorldConfig) {
        ['heat', 'scent', 'supply', 'trail', "stress"].forEach((name) => {
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
        if (!this.running) return;

        this.renderer.clear();

        if (this.settings.showHeat)
            this.renderer.drawLayer(this.layers.getLayer("heat"), this.renderer.heatLUT, "lighter");

        if (this.settings.showSupply)
            this.renderer.drawLayer(this.layers.getLayer("supply"), this.renderer.supplyLUT, "screen");
        /*
        if (this.settings.showScent) {
            this.renderer.drawLayer(this.layers.getLayer("scent"), this.renderer.scentLUT, "lighter");
        }*/

        if (this.settings.showTrails) {
            this.renderer.drawLayer(this.layers.getLayer("trail"), this.renderer.scentLUT, "lighter");
        }

        if (this.settings.showStress) {
            this.renderer.drawLayer(this.layers.getLayer("stress"), this.renderer.stressLUT, "lighter");
        }

        if (this.settings.showAgents)
            this.renderer.drawAgents(this.layers.getAgents()) ;

        this.animationFrameId = requestAnimationFrame(this.draw);
    }

    public start() {
        if (this.running) return;
        this.running = true;
        this.draw();
    }

    public stop() {
        this.running = false;
        cancelAnimationFrame(this.animationFrameId);
        this.renderer.clear();
        this.ws?.disconnect();
    }

    public findAgentByCoordinate(posX: number, posY: number) {
        const tolerance = 0.5;
        const agents = this.layers.getAgents().values();

        for (const agent of agents) {
            if (
                Math.abs(posX - agent.agentData.posX) <= tolerance &&
                Math.abs(posY - agent.agentData.posY) <= tolerance
            ) {
                return agent;
            }
        }

        return null;
    }


}